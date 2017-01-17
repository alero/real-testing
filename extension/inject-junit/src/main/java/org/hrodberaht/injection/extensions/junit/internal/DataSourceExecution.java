package org.hrodberaht.injection.extensions.junit.internal;


import org.hrodberaht.injection.config.JarUtil;
import org.hrodberaht.injection.extensions.junit.exception.DataSourceException;
import org.hrodberaht.injection.spi.DataSourceProxyInterface;
import org.hrodberaht.injection.spi.ResourceCreator;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-13 00:15:23
 * @version 1.0
 * @since 1.0
 */
public class DataSourceExecution {

    private static final String SCHEMA_PREFIX = "create_schema";
    private static final String INSERT_SCRIPT_PREFIX = "insert_script";

    private final ResourceCreator resourceCreator;


    DataSourceExecution(final ResourceCreator resourceCreator) {
        this.resourceCreator = resourceCreator;
    }

    public void addSQLSchemas(final String schemaName, final String packageBase) {

        final ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
        final ClassLoader classClassLoader = DataSourceExecution.class.getClassLoader();

        final List<File> files = new ArrayList<File>();
        List<File> foundFiles = findFiles(threadClassLoader, packageBase);
        if (foundFiles == null) {
            foundFiles = findFiles(classClassLoader, packageBase);
        }
        if (foundFiles != null) {
            files.addAll(foundFiles);
            runScripts(files, schemaName, SCHEMA_PREFIX);
            runScripts(files, schemaName, INSERT_SCRIPT_PREFIX);
        }
        findJarFiles(threadClassLoader, packageBase, schemaName);
        findJarFiles(classClassLoader, packageBase, schemaName);

    }

    private void findJarFiles(final ClassLoader classLoader, final String packageBase, final String schemaName) {

        try {
            final List<File> filesToLoad = JarUtil.findTheJarFiles(packageBase, classLoader);


            if (filesToLoad == null) {
                return;
            }
            for (File fileToLoad : filesToLoad) {
                TDDLogger.log("findJarFiles fileToLoad = " + fileToLoad);
                try(final JarFile jarFile = new JarFile(fileToLoad)) {
                    Enumeration<JarEntry> enumeration = jarFile.entries();
                    while (enumeration.hasMoreElements()) {
                        final JarEntry jarEntry = enumeration.nextElement();
                        final String jarName = jarEntry.getName();
                        if (!jarEntry.isDirectory() && jarName.startsWith(packageBase)
                                && jarName.endsWith(".sql")) {
                            TDDLogger.log("DataSourceExecution findJarFiles " + fileToLoad.getName());
                            java.io.InputStream is = jarFile.getInputStream(jarEntry);
                            BufferedReader br = new BufferedReader(new InputStreamReader(is));
                            String strLine;
                            StringBuffer stringBuffer = new StringBuffer();
                            while ((strLine = br.readLine()) != null) {
                                stringBuffer.append(strLine);
                            }
                            executeStringToSQL(schemaName, stringBuffer);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new DataSourceException("Bad file " + packageBase + " with classloader:" + classLoader);
        }

    }

    private List<File> findFiles(final ClassLoader classLoader, final String packageBase) {
        URL url = classLoader.getResource(packageBase);
        if (url == null) {
            return Collections.EMPTY_LIST;
        }
        String directoryString = url.getFile().replaceAll("%20", " ");
        File directory = new File(directoryString);
        File[] files = directory.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(files);
    }

    private void runScripts(final List<File> files, final String schemaName, final String prefix) {
        for (File file : files) {
            if (file.isFile() && file.getName().startsWith(prefix)) {
                executeScript(file, schemaName);
            }
        }
    }

    private void executeScript(final File file, final String schemaName) {
        TDDLogger.log("DataSourceExecution runScripts " + file.getName());

        try (FileInputStream fstream = new FileInputStream(file);
             DataInputStream in = new DataInputStream(fstream);
             BufferedReader br = new BufferedReader(new InputStreamReader(in))
        ){

            String strLine;
            StringBuffer stringBuffer = new StringBuffer();
            while ((strLine = br.readLine()) != null) {
                stringBuffer.append(strLine);
            }

            executeStringToSQL(schemaName, stringBuffer);

        } catch (IOException e) {
            throw new DataSourceException(e);
        }
    }

    private void executeStringToSQL(final String schemaName, final StringBuffer stringBuffer) {

        if(stringBuffer.toString().isEmpty()){
            return;
        }

        final DataSource dataSource = resourceCreator.getDataSource(schemaName);
        if (dataSource == null) {
            throw new IllegalAccessError("schemaName:" + schemaName + " does not exist ");
        }
        if(dataSource instanceof DataSourceProxyInterface) {
            DataSourceProxyInterface proxyInterface = (DataSourceProxyInterface) dataSource;
            try {
                proxyInterface.runWithConnectionAndCommit(
                        con -> runScriptForConnection(stringBuffer, con)
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            try {
                runScriptForConnection(stringBuffer, dataSource.getConnection());
            } catch (SQLException e) {
                throw new DataSourceException(e);
            }
        }
    }

    private boolean runScriptForConnection(StringBuffer stringBuffer, Connection con) {
        try (Statement stmt = con.createStatement()) {
            stmt.execute(stringBuffer.toString());
        } catch (SQLIntegrityConstraintViolationException e) {
            // Just skip this, its annoying but cant seem to fix it
        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
        return true;
    }


    boolean isInitiated(String schemaName, String packageName) {
        return this.isInitiated(packageName, schemaName, packageName);
    }

    private synchronized boolean isInitiated(final String testPackageName, final String schemaName, final String initiatedTableName) {
        DataSource dataSource = resourceCreator.getDataSource(schemaName);
        if (dataSource == null) {
            throw new IllegalAccessError("schemaName:" + schemaName + " does not exist ");
        }
        if(dataSource instanceof DataSourceProxyInterface) {
            DataSourceProxyInterface proxyInterface = (DataSourceProxyInterface) dataSource;
            try {
                return proxyInterface.runWithConnectionAndCommit(
                        con -> verifyScriptExistence(testPackageName, initiatedTableName, con));
            } catch (Exception e) {
                return false;
            }
        }else{
            try {
                return verifyScriptExistence(testPackageName, initiatedTableName, dataSource.getConnection());
            } catch (SQLException e) {
                throw new DataSourceException(e);
            }
        }
    }

    private boolean verifyScriptExistence(String testPackageName, String initiatedTableName, Connection con) {
        try {
            String tableName = cleanedName(initiatedTableName);
            String packageName = cleanedName(testPackageName);
            final String query = createQuery(tableName, packageName);
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.execute();
            }
            return false;
        } catch (SQLException e) {
            TDDLogger.log(e.getMessage());
            return true;
        }
    }

    private String createQuery(String tableName, String packageName) {
        return "create table \"" + packageName + tableName + "\" (  control_it integer )";
    }

    private String cleanedName(String schemaName) {
        return schemaName.replaceAll("/", "");
    }


}

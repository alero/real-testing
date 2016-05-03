package org.hrodberaht.injection.extensions.junit.internal;


import org.hrodberaht.injection.config.JarUtil;
import org.hrodberaht.injection.extensions.junit.util.SimpleLogger;
import org.hrodberaht.injection.spi.ResourceCreator;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static String SCHEMA_PREFIX = "create_schema";
    public static String INSERT_SCRIPT_PREFIX = "insert_script";

    private ResourceCreator resourceCreator;


    public DataSourceExecution(ResourceCreator resourceCreator) {
        this.resourceCreator = resourceCreator;
    }

    public void addSQLSchemas(String schemaName, String packageBase) {

        ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader classClassLoader = DataSourceExecution.class.getClassLoader();

        List<File> files = new ArrayList<File>();
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

    private void findJarFiles(ClassLoader classLoader, String packageBase, String schemaName) {

        try {
            List<File> filesToLoad = JarUtil.findTheJarFiles(packageBase, classLoader);


            if (filesToLoad == null) {
                return;
            }
            for (File fileToLoad : filesToLoad) {
                SimpleLogger.log("findJarFiles fileToLoad = " + fileToLoad);
                JarFile jarFile = new JarFile(fileToLoad);
                Enumeration<JarEntry> enumeration = jarFile.entries();
                while (enumeration.hasMoreElements()) {
                    JarEntry jarEntry = enumeration.nextElement();
                    String jarName = jarEntry.getName();
                    if (!jarEntry.isDirectory() && jarName.startsWith(packageBase)
                            && jarName.endsWith(".sql")) {

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
        } catch (IOException e) {
            throw new RuntimeException("Bad file " + packageBase + " with classloader:" + classLoader);
        }

    }

    private List<File> findFiles(ClassLoader classLoader, String packageBase) {
        URL url = classLoader.getResource(packageBase);
        if (url == null) {
            return null;
        }
        String directoryString = url.getFile().replaceAll("%20", " ");
        File directory = new File(directoryString);
        File[] files = directory.listFiles();
        if (files == null) {
            return new ArrayList<File>();
        }
        return Arrays.asList(files);
    }

    private void runScripts(List<File> files, String schemaName, String prefix) {
        for (File file : files) {
            if (file.isFile() && file.getName().startsWith(prefix)) {
                executeScript(file, schemaName);
            }
        }
    }

    private void executeScript(File file, String schemaName) {
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(file);

            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            StringBuffer stringBuffer = new StringBuffer();
            while ((strLine = br.readLine()) != null) {
                stringBuffer.append(strLine);
            }

            executeStringToSQL(schemaName, stringBuffer);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeStringToSQL(String schemaName, StringBuffer stringBuffer) {
        DataSource dataSource = resourceCreator.getDataSource(schemaName);
        if (dataSource == null) {
            throw new IllegalAccessError("schemaName:" + schemaName + " does not exist ");
        }

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();) {
            stmt.execute(stringBuffer.toString());
            if (dataSource instanceof DataSourceProxy) {
                DataSourceProxy dataSourceProxy = (DataSourceProxy) dataSource;
                dataSourceProxy.commitDataSource();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean isInitiated(String schemaName, String packageName) {
        return this.isInitiated(packageName, schemaName, packageName);
    }

    public boolean isInitiated(String testPackageName, String schemaName, String initiatedTableName) {
        DataSource dataSource = resourceCreator.getDataSource(schemaName);
        if (dataSource == null) {
            throw new IllegalAccessError("schemaName:" + schemaName + " does not exist ");
        }

        try (Connection connection = dataSource.getConnection()) {

            initiatedTableName = cleanedName(initiatedTableName);
            testPackageName = cleanedName(testPackageName);
            try (PreparedStatement pstmt = connection.prepareStatement("create table " + testPackageName + initiatedTableName + " (  control_it integer )")) {
                pstmt.execute();
            }
            if (dataSource instanceof DataSourceProxy) {
                DataSourceProxy dataSourceProxy = (DataSourceProxy) dataSource;
                dataSourceProxy.commitDataSource();
            }
            return false;
        } catch (SQLException e) {
            return true;
        }

    }

    private String cleanedName(String schemaName) {
        return schemaName.replaceAll("/", "");
    }


}

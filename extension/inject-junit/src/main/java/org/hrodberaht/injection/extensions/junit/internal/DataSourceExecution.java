package org.hrodberaht.injection.extensions.junit.internal;


import org.hrodberaht.injection.config.JarUtil;
import org.hrodberaht.injection.extensions.junit.util.SimpleLogger;
import org.hrodberaht.injection.spi.ResourceCreator;

import java.io.*;
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

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();


        List<File> files = new ArrayList<File>();
        files.addAll(findFiles(classLoader, packageBase));
        runScripts(files, schemaName, SCHEMA_PREFIX);
        runScripts(files, schemaName, INSERT_SCRIPT_PREFIX);


        findJarFiles(classLoader, packageBase, schemaName);

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
        DataSourceProxy dataSourceProxy = (DataSourceProxy) resourceCreator.getDataSource(schemaName);
        Statement stmt = null;
        try {
            Connection connection = dataSourceProxy.getConnection();
            stmt = connection.createStatement();
            stmt.execute(stringBuffer.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {

            }
            dataSourceProxy.commitDataSource();
        }
    }


    public boolean isInitiated(String testPackageName, String schemaName) {
        return this.isInitiated(testPackageName, schemaName, testPackageName);
    }

    public boolean isInitiated(String testPackageName, String schemaName, String initiatedTableName) {
        DataSourceProxy dataSourceProxy = (DataSourceProxy) resourceCreator.getDataSource(schemaName);
        PreparedStatement pstmt = null;
        try {
            if (dataSourceProxy == null) {
                throw new IllegalAccessError("schemaName:" + schemaName + " does not exist ");
            }
            Connection connection = dataSourceProxy.getConnection();
            pstmt = connection.prepareStatement("create table " + testPackageName + initiatedTableName + " (  control_it integer)");
            pstmt.execute();
            dataSourceProxy.commitDataSource();
            return false;
        } catch (SQLException e) {
            return true;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {

            }

        }
    }


}

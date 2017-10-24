/*
 * Copyright (c) 2017 org.hrodberaht
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hrodberaht.injection.plugin.junit.datasource;


import org.hrodberaht.injection.core.config.JarUtil;
import org.hrodberaht.injection.plugin.datasource.DataSourceException;
import org.hrodberaht.injection.plugin.datasource.DataSourceProxyInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * 2010-okt-13 00:15:23
 * @version 1.0
 * @since 1.0
 */
class DataSourceExecution {

    private static final Logger LOG = LoggerFactory.getLogger(DataSourceExecution.class);

    private static final String SCHEMA_PREFIX = "create_schema";
    private static final String INSERT_SCRIPT_PREFIX = "insert_script";

    private final DataSource dataSource;

    DataSourceExecution(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    void addSQLSchemas(final String schemaName, final String packageBase) {

        final ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
        final ClassLoader classClassLoader = DataSourceExecution.class.getClassLoader();

        final List<File> files = new ArrayList<>();
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
                LOG.debug("findJarFiles fileToLoad = {}", fileToLoad);
                try (final JarFile jarFile = new JarFile(fileToLoad)) {
                    Enumeration<JarEntry> enumeration = jarFile.entries();
                    handleJarEntries(packageBase, schemaName, fileToLoad, jarFile, enumeration);
                }
            }
        } catch (IOException e) {
            throw new DataSourceException("Bad file " + packageBase + " with classloader:" + classLoader, e);
        }

    }

    private void handleJarEntries(String packageBase, String schemaName, File fileToLoad, JarFile jarFile, Enumeration<JarEntry> enumeration) throws IOException {
        while (enumeration.hasMoreElements()) {
            final JarEntry jarEntry = enumeration.nextElement();
            final String jarName = jarEntry.getName();
            if (!jarEntry.isDirectory() && jarName.startsWith(packageBase)
                    && jarName.endsWith(".sql")) {
                LOG.debug("DataSourceExecution findJarFiles {}", fileToLoad.getName());
                java.io.InputStream is = jarFile.getInputStream(jarEntry);
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String strLine;
                StringBuilder stringBuffer = new StringBuilder();
                while ((strLine = br.readLine()) != null) {
                    stringBuffer.append(strLine);
                }
                executeStringToSQL(schemaName, stringBuffer);
            }
        }
    }

    private List<File> findFiles(final ClassLoader classLoader, final String packageBase) {
        String classLoadingCompatible = stabilize(packageBase);
        LOG.info("loading resources from {}", classLoadingCompatible);
        URL url = classLoader.getResource(classLoadingCompatible);
        if (url == null) {
            LOG.info("found no resources at {}", classLoadingCompatible);
            return Collections.emptyList();
        }
        LOG.info("found some resources at {}", classLoadingCompatible);
        String directoryString = url.getFile().replaceAll("%20", " ");
        File directory = new File(directoryString);
        File[] files = directory.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(files);
    }

    private String stabilize(String packageBase) {
        String newValue = packageBase.contains(".") ? packageBase.replaceAll("\\.", "/") : packageBase;
        return newValue.startsWith("/") ? newValue.substring(1, newValue.length()) : newValue;
    }

    private void runScripts(final List<File> files, final String schemaName, final String prefix) {
        for (File file : files) {
            if (file.isFile() && file.getName().startsWith(prefix)) {
                executeScript(file, schemaName);
            }
        }
    }

    private void executeScript(final File file, final String schemaName) {
        LOG.info("DataSourceExecution runScripts " + file.getName());

        try (FileInputStream fstream = new FileInputStream(file);
             DataInputStream in = new DataInputStream(fstream);
             BufferedReader br = new BufferedReader(new InputStreamReader(in))
        ) {

            String strLine;
            StringBuilder stringBuffer = new StringBuilder();
            while ((strLine = br.readLine()) != null) {
                stringBuffer.append(strLine);
            }

            executeStringToSQL(schemaName, stringBuffer);

        } catch (IOException e) {
            throw new DataSourceException(e);
        }
    }

    private void executeStringToSQL(final String schemaName, final StringBuilder stringBuffer) {

        if (stringBuffer.toString().isEmpty()) {
            return;
        }


        if (dataSource == null) {
            throw new IllegalAccessError("schemaName:" + schemaName + " does not exist ");
        }
        if (dataSource instanceof DataSourceProxyInterface) {
            DataSourceProxyInterface proxyInterface = (DataSourceProxyInterface) dataSource;
            try {
                proxyInterface.runWithConnectionAndCommit(
                        con -> runScriptForConnection(stringBuffer, con)
                );
            } catch (Exception e) {
                throw new DataSourceException(e);
            }
        } else {
            try {
                runScriptForConnection(stringBuffer, dataSource.getConnection());
            } catch (SQLException e) {
                throw new DataSourceException(e);
            }
        }
    }

    private boolean runScriptForConnection(StringBuilder stringBuffer, Connection con) {
        try (Statement stmt = con.createStatement()) {
            stmt.execute(stringBuffer.toString());
        } catch (SQLIntegrityConstraintViolationException e) {
            // Just skip this, its annoying but cant seem to fix it
            LOG.debug("hidden error: " + e.getMessage());
        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
        return true;
    }

    synchronized boolean isInitiated(final String testPackageName, final String initiatedTableName) {

        if (dataSource == null) {
            throw new IllegalAccessError("dataSource is null ");
        }
        if (dataSource instanceof DataSourceProxyInterface) {
            DataSourceProxyInterface proxyInterface = (DataSourceProxyInterface) dataSource;
            try {
                return proxyInterface.runWithConnectionAndCommit(
                        con -> verifyScriptExistence(testPackageName, initiatedTableName, con));
            } catch (Exception e) {
                return false;
            }
        } else {
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

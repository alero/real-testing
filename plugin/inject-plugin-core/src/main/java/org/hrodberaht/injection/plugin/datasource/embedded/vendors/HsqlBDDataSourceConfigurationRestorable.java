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

package org.hrodberaht.injection.plugin.datasource.embedded.vendors;

import org.hrodberaht.injection.plugin.datasource.embedded.DataSourceConfiguration;
import org.hrodberaht.injection.plugin.junit.ResourceWatcher;
import org.hrodberaht.injection.plugin.junit.datasource.DataSourceProxyInterface;
import org.hrodberaht.injection.plugin.junit.datasource.DataSourceRuntimeException;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class HsqlBDDataSourceConfigurationRestorable implements DataSourceConfiguration {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(HsqlBDDataSourceConfigurationRestorable.class);
    private static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";
    private static final String JDBC_USERNAME = "sa";


    private final String dbName;
    private final ResourceWatcher resourceWatcher;
    private HSQLDriverManager driverManager = null;

    private DatasourceBackupRestore datasourceBackupRestore;

    public HsqlBDDataSourceConfigurationRestorable(String dbName, ResourceWatcher resourceWatcher) {
        this.dbName = dbName;
        this.resourceWatcher = resourceWatcher;
    }

    @Override
    public TestDataSourceWrapper getTestDataSource(String name) {
        try {
            initateManager();
        } catch (ClassNotFoundException e) {
            throw new DataSourceRuntimeException(e);
        }
        return datasourceBackupRestore.getTestDataSource(name);
    }

    private void initateManager() throws ClassNotFoundException {

        if (driverManager != null) {
            return;
        }

        Class.forName(JDBC_DRIVER);
        driverManager = new HSQLBasicDriverManager();
        datasourceBackupRestore = new PlainHSqlBackupRestore(driverManager);

    }

    interface HSQLDriverManager extends VendorDriverManager{
        PlainConnection getInnerConnection() throws SQLException;
    }

    class HSQLBasicDriverManager implements HSQLDriverManager {

        private Map<String, TestConnection> borrowedTest = new HashMap<>();
        private Map<String, TestConnection> borrowedPlain = new HashMap<>();

        @Override
        public TestConnection getConnection() throws SQLException {
            return createConnection();
        }

        private TestConnection createConnection() throws SQLException {
            checkForOpenChanges(borrowedPlain);
            return borrowConnnection(new TestConnection(createSQLConnextion()), borrowedTest);
        }

        private void checkForOpenChanges(Map<String, TestConnection> connectionMap) {
            if(!connectionMap.isEmpty()){
                connectionMap.values().forEach(testConnection -> {
                    if(testConnection.isOpenChanges()){
                        throw new IllegalAccessError("Open changes in other connection");
                    }
                });
            }
        }

        private <T extends TestConnection> T borrowConnnection(T testConnection, Map<String, TestConnection> borrowed) throws SQLException {
            if(!borrowed.isEmpty()){
                throw new IllegalAccessError("Not allowed to borrow new connection until old one is closed");
            }
            testConnection.setBorrowed(borrowed);
            borrowed.put(testConnection.getUuid(), testConnection);
            return testConnection;
        }

        private Connection createSQLConnextion() throws SQLException {
            return DriverManager.getConnection(datasourceBackupRestore.jdbcUrl() + dbName, JDBC_USERNAME, "");
        }

        @Override
        public PlainConnection getInnerConnection() throws SQLException {
            checkForOpenChanges(borrowedTest);
            return borrowConnnection(new PlainConnection(createSQLConnextion()), borrowedPlain);
        }


    }

    /**
     * See http://hsqldb.org/doc/guide/management-chapt.html#mtc_online_backup
     * Need to create and restore the snapshot better ....
     *
     * @param name
     */

    @Override
    public void createSnapshot(String name) {

        datasourceBackupRestore.createSnapshot(name);

    }

    public void loadSnapshot(String name) {

        datasourceBackupRestore.loadSnapshot(name);

    }

    @Override
    public boolean runWithConnectionAndCommit(DataSourceProxyInterface.ConnectionRunner connectionRunner) {
        PlainConnection connection = null;
        try {
            connection = driverManager.getInnerConnection();
            LOG.info("-- runWithConnectionAndCommit -- Connection {}", connection);
            connection.setAutoCommit(false);
            boolean returnBool = connectionRunner.run(connection);
            connection.commit();
            return returnBool;
        } catch (Exception e) {
            throw new DataSourceRuntimeException(e);
        }finally {
            if(connection != null){
                connection.dontFailClose();
            }
        }
    }


}

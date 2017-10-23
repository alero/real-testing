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

import org.hrodberaht.injection.plugin.datasource.DataSourceProxyInterface;
import org.hrodberaht.injection.plugin.datasource.embedded.DataSourceConfiguration;
import org.hrodberaht.injection.plugin.junit.ResourceWatcher;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;

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

    public Connection initateConnection() throws ClassNotFoundException, SQLException {

        if (driverManager != null) {
            return driverManager.getConnection();
        }

        Class.forName(JDBC_DRIVER);
        driverManager = new HSQLBasicDriverManager();
        datasourceBackupRestore = new PlainHSqlBackupRestore(driverManager);
        return driverManager.getConnection();

    }

    interface HSQLDriverManager {
        Connection getConnection() throws SQLException;
    }

    class HSQLBasicDriverManager implements HSQLDriverManager {

        @Override
        public Connection getConnection() throws SQLException {
            LOG.debug("-- Creating Connection HsqlBDDataSourceConfigurationRestorable from mem");
            try {
                return DriverManager.getConnection(datasourceBackupRestore.jdbcUrl() + dbName, JDBC_USERNAME, "");
            } catch (SQLInvalidAuthorizationSpecException e) {
                throw e;
            }
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
        try (Connection conn = initateConnection()) {
            boolean returnBool = connectionRunner.run(conn);
            conn.commit();
            return returnBool;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

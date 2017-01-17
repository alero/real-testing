package org.hrodberaht.injection.extensions.junit.internal.embedded.vendors;

import org.hrodberaht.injection.extensions.junit.internal.TDDLogger;
import org.hrodberaht.injection.extensions.junit.internal.embedded.DataSourceConfiguration;
import org.hrodberaht.injection.extensions.junit.internal.embedded.ResourceWatcher;
import org.hrodberaht.injection.spi.DataSourceProxyInterface;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.Scanner;
import java.util.logging.Logger;

public class HsqlBDDataSourceConfigurationRestorable implements DataSourceConfiguration {

    private static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";
    private static final String JDBC_USERNAME = "sa";


    private String dbName = null;
    private ResourceWatcher resourceWatcher;

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
            TDDLogger.log("-- Creating Connection HsqlBDDataSourceConfigurationRestorable from mem");
            try {
                return DriverManager.getConnection(datasourceBackupRestore.jdbcUrl() + dbName, JDBC_USERNAME, "");
            }catch (SQLInvalidAuthorizationSpecException e){
                throw e;
            }
        }
    }

    /**
     * See http://hsqldb.org/doc/guide/management-chapt.html#mtc_online_backup
     * Need to create and restore the snapshot better ....
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

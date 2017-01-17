package org.hrodberaht.injection.extensions.junit.internal.embedded.vendors;

import org.hrodberaht.injection.extensions.junit.internal.embedded.DataSourceConfiguration;
import org.hrodberaht.injection.spi.DataSourceProxyInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HsqlBDDataSourceConfigurationMem implements DataSourceConfiguration {

    public String JDBC_DRIVER = "org.hsqldb.jdbcDriver";
    public String JDBC_URL = "jdbc:hsqldb:mem:test";
    public String JDBC_USERNAME = "sa";

    private String dbName = null;

    public HsqlBDDataSourceConfigurationMem(String dbName) {
        this.dbName = dbName;
    }

    public Connection initateConnection() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        return DriverManager.getConnection(JDBC_URL + dbName, JDBC_USERNAME, "");
    }

    @Override
    public void createSnapshot(String name) {
        throw new IllegalAccessError("not supported");
    }

    @Override
    public void loadSnapshot(String name) {
        throw new IllegalAccessError("not supported");
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

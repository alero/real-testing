package org.hrodberaht.injection.extensions.junit.internal.embedded.vendors;

import org.hrodberaht.injection.extensions.junit.internal.embedded.DataSourceConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HsqlBDDataSourceConfigurationMem implements DataSourceConfiguration {

    public String JDBC_DRIVER = "org.hsqldb.jdbcDriver";
    public String JDBC_URL = "jdbc:hsqldb:mem:test";
    public String JDBC_USERNAME = "sa";
    public String JDBC_PASSWORD = "";

    private String dbName = null;

    public HsqlBDDataSourceConfigurationMem(String dbName) {
        this.dbName = dbName;
    }

    public Connection initateConnection() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        return DriverManager.getConnection(JDBC_URL + dbName, JDBC_USERNAME, JDBC_PASSWORD);
    }

    @Override
    public void createSnapshot() {
        throw new IllegalAccessError("not supported");
    }

    @Override
    public void loadSnapshot() {
        throw new IllegalAccessError("not supported");
    }

}

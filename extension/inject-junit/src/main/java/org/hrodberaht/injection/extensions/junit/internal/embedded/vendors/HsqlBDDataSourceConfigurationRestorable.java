package org.hrodberaht.injection.extensions.junit.internal.embedded.vendors;

import org.hrodberaht.injection.extensions.junit.internal.TDDLogger;
import org.hrodberaht.injection.extensions.junit.internal.embedded.DataSourceConfiguration;
import org.hrodberaht.injection.extensions.junit.internal.embedded.PersistenceResource;
import org.hrodberaht.injection.extensions.junit.internal.embedded.ResourceWatcher;
import org.hrodberaht.injection.spi.DataSourceProxyInterface;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class HsqlBDDataSourceConfigurationRestorable implements DataSourceConfiguration {

    private static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";
    private static final String JDBC_BASEURL_NEW = "jdbc:hsqldb:mem:";
    private static final String JDBC_BASEURL_RESTORE = "jdbc:hsqldb:file:";
    private static final String JDBC_PROPS = "files_readonly=true";
    private static final String JDBC_USERNAME = "sa";
    private static final String JDBC_PASSWORD = "hsql";

    private String dbName = null;
    private ResourceWatcher resourceWatcher;
    private PersistenceResource resource;
    private DataSourceProxyInterface dataSource;

    private HSQLDriverManager driverManager = null;

    public HsqlBDDataSourceConfigurationRestorable(String dbName, ResourceWatcher resourceWatcher,
                                                   PersistenceResource resource, DataSourceProxyInterface dataSource) {
        this.dbName = dbName;
        this.resourceWatcher = resourceWatcher;
        this.resource = resource;
        this.dataSource = dataSource;
    }

    public Connection initateConnection() throws ClassNotFoundException, SQLException {

        if (driverManager != null) {
            return driverManager.getConnection();
        }

        Class.forName(JDBC_DRIVER);
        if (useMemStore()) {
            driverManager = new HSQLBasicDriverManager();
            return driverManager.getConnection();
        } else {
            driverManager = new HSQLBasicDriverManager();
            return driverManager.getConnection();
        }
    }

    private interface HSQLDriverManager {
        Connection getConnection() throws SQLException;
    }

    private class HSQLBasicDriverManager implements HSQLDriverManager {

        @Override
        public Connection getConnection() throws SQLException {
            TDDLogger.log("-- Creating Connection HsqlBDDataSourceConfigurationRestorable from mem");
            return DriverManager.getConnection(JDBC_BASEURL_NEW + dbName, JDBC_USERNAME, JDBC_PASSWORD);
        }
    }

    private class HSQLRestoredDriverManager implements HSQLDriverManager {

        @Override
        public Connection getConnection() throws SQLException {
            TDDLogger.log("-- Creating Connection HsqlBDDataSourceConfigurationRestorable from file:" + resource.getName());
            String url = JDBC_BASEURL_RESTORE + resource.getName() + dbName + ";" + JDBC_PROPS;
            return DriverManager.getConnection(url, JDBC_USERNAME, JDBC_PASSWORD);
        }
    }


    private boolean useMemStore() {
        return !resource.exists();
    }

    @Override
    public void createSnapshot() {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            String backup = "SCRIPT '" + resource.getName() + "'";
            jdbcTemplate.execute(backup);
        } finally {
            dataSource.clearDataSource();
        }
    }

    public void loadSnapshot() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        File file = new File(resource.getName());
        try {
            TDDLogger.log("---- BACKUPFILE CONTENT " + readFile(file));
            readFile(file, jdbcTemplate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readFile(File file) throws IOException {
        StringBuilder fileContents = new StringBuilder((int) file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");

        try {
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }

    private String readFile(File file, JdbcTemplate jdbcTemplate) throws IOException {
        StringBuilder fileContents = new StringBuilder((int) file.length());
        Scanner scanner = new Scanner(file);
        // String lineSeparator = System.getProperty("line.separator");

        try {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (shouldExecuteLine(line)) {
                    jdbcTemplate.execute(line);
                }
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }

    private boolean shouldExecuteLine(String line) {
        return !
                ("CREATE SCHEMA PUBLIC AUTHORIZATION DBA".equals(line)
                        || line.contains("CREATE USER SA PASSWORD DIGEST")
                        || line.contains("GRANT DBA TO SA")
                        || line.equals("SET SCHEMA SYSTEM_LOBS")
                        || line.contains("INSERT INTO BLOCKS VALUES")

                );
    }

}

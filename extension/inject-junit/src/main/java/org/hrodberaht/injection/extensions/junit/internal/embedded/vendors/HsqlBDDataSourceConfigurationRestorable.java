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
    private static final String JDBC_BASEURL_NEW = "jdbc:hsqldb:mem:";
    private static final String JDBC_USERNAME = "sa";
    private static final String JDBC_PASSWORD = "";

    private static final String lineSeparator = System.getProperty("line.separator");

    private String dbName = null;
    private ResourceWatcher resourceWatcher;

    private HSQLDriverManager driverManager = null;

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
        return driverManager.getConnection();

    }

    private interface HSQLDriverManager {
        Connection getConnection() throws SQLException;
    }

    private class HSQLBasicDriverManager implements HSQLDriverManager {

        @Override
        public Connection getConnection() throws SQLException {
            TDDLogger.log("-- Creating Connection HsqlBDDataSourceConfigurationRestorable from mem");
            try {
                return DriverManager.getConnection(JDBC_BASEURL_NEW + dbName, JDBC_USERNAME, JDBC_PASSWORD);
            }catch (SQLInvalidAuthorizationSpecException e){
                throw e;
            }
        }
    }


    @Override
    public void createSnapshot(String name) {
        try (Connection connection = driverManager.getConnection();){

            JdbcTemplate jdbcTemplate = new JdbcTemplate(new DataSourceWrapper(connection));
            String backup = "SCRIPT '" + name + "'";
            jdbcTemplate.execute(backup);
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadSnapshot(String name) {
        try (Connection connection = driverManager.getConnection();){

            JdbcTemplate jdbcTemplate = new JdbcTemplate(new DataSourceWrapper(connection));
            File file = new File(name);
                TDDLogger.log("---- BACKUPFILE CONTENT " + readFile(file));
            readFile(file, jdbcTemplate);
            connection.commit();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
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

    private String readFile(File file) throws IOException {
        StringBuilder fileContents = new StringBuilder((int) file.length());
        Scanner scanner = new Scanner(file);

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


    private class DataSourceWrapper implements javax.sql.DataSource{

        private final Connection connection;
        private final Connection proxy;

        public DataSourceWrapper(Connection connection) {
            this.connection = connection;
            proxy = createNoCloseProxy();
        }

        private Connection createNoCloseProxy() {
            InvocationHandler invocationHandler = (proxy, method, args) -> {
                if (method.getName().equals("close")) {
                    // do nothing
                    return null;
                }
                return method.invoke(connection, args);
            };
            return (Connection) Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(), new Class[]{Connection.class}, invocationHandler
            );
        }

        @Override
        public Connection getConnection() throws SQLException {
            return proxy;
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return proxy;
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return null;
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false;
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return null;
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {

        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {

        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return 0;
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return null;
        }
    }

}

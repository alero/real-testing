package org.hrodberaht.inject.extension.tdd.internal;

import org.hrodberaht.inject.spi.DataSourceProxyInterface;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Unit Test EJB (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 22:34:53
 * @version 1.0
 * @since 1.0
 */
public class DataSourceProxy implements DataSourceProxyInterface {

    private static Map<String, String> DB_NAME_MAPPING = new HashMap<String, String>();

    private final ThreadLocal<ConnectionHandler> threadLocal = new ThreadLocal<ConnectionHandler>();

    public String JDBC_DRIVER = "org.hsqldb.jdbcDriver";
    public String JDBC_URL = "jdbc:hsqldb:mem:test";
    public String JDBC_USERNAME = "sa";
    public String JDBC_PASSWORD = "";

    private String dbName = null;

    public DataSourceProxy(String dbName) {
        if (DB_NAME_MAPPING.containsKey(dbName)) {
            this.dbName = DB_NAME_MAPPING.get(dbName);
        } else {
            this.dbName = dbName;
        }
    }


    public static void addDataSourceNameMapping(String dataSourceName, String databaseName) {
        DB_NAME_MAPPING.put(dataSourceName, databaseName);
    }

    public void clearDataSource() {
        ConnectionHandler connection = threadLocal.get();
        if (connection != null) {
            try {
                connection.conn.rollback();
                connection.conn.close();
                closeRecursively(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            threadLocal.remove();
        }
    }

    private void closeRecursively(ConnectionHandler connection) {
        for (ConnectionHandler children : connection.children) {
            try {
                children.conn.rollback();
                children.conn.close();
                closeRecursively(children);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void commitDataSource() {
        ConnectionHandler connection = threadLocal.get();
        if (connection != null) {
            try {
                connection.conn.commit();
                connection.conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            threadLocal.remove();
        }
    }

    public void finalizeConnection(Connection connection) {
        ConnectionHandler localConnection = threadLocal.get();
        if (localConnection != null && localConnection.parent != null) {
            threadLocal.set(localConnection.parent);
        }
    }

    public void forceCreateNewConnection() {
        ConnectionHandler connection = threadLocal.get();
        if (connection != null) {
            threadLocal.set(new ConnectionHandler(connection));
        }
    }

    public Connection getConnection() throws SQLException {
        ConnectionHandler connection = threadLocal.get();
        if (connection != null && connection.hasConnection()) {
            return connection.proxy;
        }
        try {
            ConnectionHandler connectionHandler = new ConnectionHandler();
            Class.forName(JDBC_DRIVER);
            final Connection conn = DriverManager.getConnection(JDBC_URL + dbName, JDBC_USERNAME, JDBC_PASSWORD);
            conn.setAutoCommit(false);
            InvocationHandler invocationHandler = new InvocationHandler() {
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (method.getName().equals("close")) {
                        // do nothing
                        return null;
                    } else if (method.getName().equals("commit")) {
                        // do nothing
                        return null;
                    }
                    return method.invoke(conn, args);
                }
            };

            Connection proxy = (Connection) Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(), new Class[]{Connection.class}, invocationHandler
            );
            if (connection != null && !connection.hasConnection()) {
                connection.conn = conn;
                connection.proxy = proxy;
            } else {
                connectionHandler.conn = conn;
                connectionHandler.proxy = proxy;
                threadLocal.set(connectionHandler);
            }
            return proxy;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }

    public PrintWriter getLogWriter() throws SQLException {
        return new PrintWriter(System.out);
    }

    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    public void setLoginTimeout(int seconds) throws SQLException {

    }

    public int getLoginTimeout() throws SQLException {
        return 600;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }


    private class ConnectionHandler {
        private Connection conn;
        private Connection proxy;

        private ConnectionHandler parent;
        private Collection<ConnectionHandler> children = new ArrayList<ConnectionHandler>();


        private ConnectionHandler() {
        }

        private ConnectionHandler(ConnectionHandler parent) {
            this.parent = parent;
            parent.children.add(this);
        }

        private boolean hasConnection() {
            return conn != null;
        }
    }
}

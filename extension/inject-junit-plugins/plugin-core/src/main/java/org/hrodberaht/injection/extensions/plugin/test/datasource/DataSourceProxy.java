package org.hrodberaht.injection.extensions.plugin.test.datasource;

import org.hrodberaht.injection.extensions.plugin.datasource.embedded.DataSourceConfigFactory;
import org.hrodberaht.injection.extensions.plugin.datasource.embedded.DataSourceConfiguration;
import org.hrodberaht.injection.extensions.plugin.datasource.embedded.ResourceWatcher;
import org.hrodberaht.injection.spi.DataSourceProxyInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 22:34:53
 * @version 1.0
 * @since 1.0
 */
public class DataSourceProxy implements DataSourceProxyInterface {

    private static final Logger LOG = LoggerFactory.getLogger(DataSourceProxy.class);

    private static Map<String, String> DB_NAME_MAPPING = new ConcurrentHashMap<String, String>();

    private static final Map<String, Connection> CONNECTIONS = new ConcurrentHashMap<>();

    private final InheritableThreadLocal<ConnectionHandler> threadLocal = new InheritableThreadLocal<ConnectionHandler>();


    private DataSourceConfiguration dataSourceConfiguration;
    private String dbName = null;
    private ResourceWatcher resourceWatcher;
    private DataSourceConfigFactory dataSourceConfigFactory;

    private ProxyResourceCreator.DataSourceProvider provider;
    private ProxyResourceCreator.DataSourcePersistence persistence;

    public DataSourceProxy(String dbName,
                           ProxyResourceCreator.DataSourceProvider provider,
                           ProxyResourceCreator.DataSourcePersistence persistence,
                           ResourceWatcher resourceWatcher) {
        this.resourceWatcher = resourceWatcher;
        this.provider = provider;
        this.persistence = persistence;
        if (DB_NAME_MAPPING.containsKey(dbName)) {
            this.dbName = DB_NAME_MAPPING.get(dbName);
        } else {
            this.dbName = dbName;
        }
        init();
    }

    private void init() {
        if (resourceWatcher == null) {
            resourceWatcher = () -> false;
        }
        dataSourceConfigFactory = new DataSourceConfigFactory(this, resourceWatcher, dbName);
        dataSourceConfiguration = dataSourceConfigFactory.createConfiguration(provider, persistence);
    }

    public void createSnapshot(String name) {
        dataSourceConfiguration.createSnapshot(name);
    }

    @Override
    public boolean runWithConnectionAndCommit(ConnectionRunner connectionRunner) {
        return dataSourceConfiguration.runWithConnectionAndCommit(connectionRunner);
    }

    public void loadSnapshot(String name) {
        dataSourceConfiguration.loadSnapshot(name);
    }

    public static void addDataSourceNameMapping(String dataSourceName, String databaseName) {
        DB_NAME_MAPPING.put(dataSourceName, databaseName);
    }

    public void clearDataSource() {
        ConnectionHandler connection = threadLocal.get();
        if (connection != null) {
            rollbackAndClose(connection);
            threadLocal.remove();
        }
    }

    private void rollbackRecursively(ConnectionHandler connection) {
        for (ConnectionHandler children : connection.children) {
            rollbackAndClose(children);
        }
    }

    private void rollbackAndClose(ConnectionHandler connectionHandler) {
        try {
            connectionHandler.conn.rollback();
            connectionHandler.conn.close();
            CONNECTIONS.remove(connectionHandler.conn.toString());
            LOG.debug("rollback/close Connection " + connectionHandler + " Thread:" + Thread.currentThread());
            rollbackRecursively(connectionHandler);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void commitDataSource() {
        ConnectionHandler connection = threadLocal.get();
        if (connection != null) {
            commitAndClose(connection);
            threadLocal.remove();
        }
    }

    @Override
    public Connection getNativeConnection() {
        return threadLocal.get().conn;
    }

    private void commitRecursively(ConnectionHandler connection) {
        for (ConnectionHandler children : connection.children) {
            commitAndClose(children);
        }
    }

    private void commitAndClose(ConnectionHandler connectionHandler) {
        try {
            CONNECTIONS.remove(connectionHandler.conn.toString());
            connectionHandler.conn.commit();
            connectionHandler.conn.close();
            LOG.debug("commit/close Connection " + connectionHandler + " Thread:" + Thread.currentThread());
            commitRecursively(connectionHandler);
        } catch (SQLNonTransientConnectionException exception) {
            if (exception.getMessage().contains("connection does not exist")) {
                LOG.debug("connection closed from the outside");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        final ConnectionHandler connection = threadLocal.get();

        if (connection != null && connection.hasConnection()) {
            LOG.debug("reusing Connection " + connection + " Thread:" + Thread.currentThread());
            return connection.proxy;
        }
        try {
            final ConnectionHandler connectionHandler = new ConnectionHandler();
            final Connection conn = dataSourceConfiguration.initateConnection();
            CONNECTIONS.put(conn.toString(), conn);
            conn.setAutoCommit(false);
            InvocationHandler invocationHandler = (proxy, method, args) -> {
                if (method.getName().equals("close")) {
                    // do nothing
                    return null;
                } else if (method.getName().equals("commit")) {
                    // do nothing
                    return null;
                }
                return method.invoke(conn, args);
            };

            final Connection proxy = (Connection) Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(), new Class[]{Connection.class}, invocationHandler
            );
            if (connection != null && !connection.hasConnection()) {
                connection.conn = conn;
                connection.proxy = proxy;
                LOG.debug(" new empty Connection " + connectionHandler);
            } else {
                connectionHandler.conn = conn;
                connectionHandler.proxy = proxy;
                LOG.debug(" new Connection " + connectionHandler + " Thread:" + Thread.currentThread());
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

    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
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

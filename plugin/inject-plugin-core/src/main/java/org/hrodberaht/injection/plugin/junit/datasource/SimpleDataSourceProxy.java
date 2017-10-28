package org.hrodberaht.injection.plugin.junit.datasource;

import org.hrodberaht.injection.plugin.datasource.embedded.DataSourceConfigFactory;
import org.hrodberaht.injection.plugin.datasource.embedded.DataSourceConfiguration;
import org.hrodberaht.injection.plugin.datasource.embedded.vendors.TestDataSourceWrapper;
import org.hrodberaht.injection.plugin.junit.ResourceWatcher;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class SimpleDataSourceProxy implements DataSourceProxyInterface {

    private final DataSourceConfiguration dataSourceConfiguration;
    private final DataSourceConfigFactory dataSourceConfigFactory;
    private final TestDataSourceWrapper dataSource;

    SimpleDataSourceProxy(String dataSourceName,
                                 ProxyResourceCreator.DataSourceProvider provider,
                                 ProxyResourceCreator.DataSourcePersistence persistence,
                                 ResourceWatcher resourceWatcher) {
        dataSourceConfigFactory = new DataSourceConfigFactory(this, getResourceWatcher(resourceWatcher), dataSourceName);
        dataSourceConfiguration = dataSourceConfigFactory.createConfiguration(provider, persistence);
        dataSource = dataSourceConfiguration.getTestDataSource(dataSourceName);
    }

    private ResourceWatcher getResourceWatcher(ResourceWatcher resourceWatcher) {
        if (resourceWatcher == null) {
            resourceWatcher = () -> false;
        }
        return resourceWatcher;
    }

    @Override
    public void clearDataSource() {
        dataSource.clearDataSource();
    }

    @Override
    public void commitDataSource() {
        dataSource.commitNativeConnection();
    }

    @Override
    public void loadSnapshot(String name) {
        dataSourceConfiguration.loadSnapshot(name);
    }

    @Override
    public void createSnapshot(String name) {
        dataSourceConfiguration.createSnapshot(name);
    }

    @Override
    public boolean runWithConnectionAndCommit(ConnectionRunner connectionRunner) throws Exception {
        return dataSourceConfiguration.runWithConnectionAndCommit(connectionRunner);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return dataSource.getConnection(username, password);
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

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public String toString() {
        return dataSource.toString();
    }
}

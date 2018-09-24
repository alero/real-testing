package org.hrodberaht.injection.plugin.datasource.embedded.vendors;

import org.hrodberaht.injection.plugin.datasource.DataSourceException;
import org.hrodberaht.injection.plugin.junit.datasource.DataSourceRuntimeException;
import org.slf4j.LoggerFactory;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Executor;

public class TestConnection implements Connection {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(TestConnection.class);

    private final Connection connection;
    private final String uuid = UUID.randomUUID().toString();
    private Map<String, TestConnection> borrowed;

    private boolean openChanges;

    public TestConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isOpenChanges() {
        return openChanges;
    }

    public void setOpenChanges(boolean openChanges) {
        this.openChanges = openChanges;
    }

    public String getUuid() {
        return uuid;
    }

    public void dontFailClose() {
        try {
            closeIt();
        } catch (DataSourceRuntimeException e) {
        }
    }

    public void dontFailRollback() {
        try {
            rollback();
        } catch (SQLException e) {
        }
    }

    @Override
    public Statement createStatement() throws SQLException {
        LOG.info("createStatement {}", this);
        try {
            return new TestStatement(this, connection.createStatement());
        } catch (Throwable e) {
            throw new DataSourceException(e);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        LOG.info("prepareStatement {} - {}", this, sql);
        return new TestPreparedStatement(this, connection.prepareStatement(sql));
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        openChanges = true;
        return connection.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return connection.nativeSQL(sql);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return connection.getAutoCommit();
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }

    public void commitIt() throws SQLException {
        LOG.info("commitIt - {}", this);
        connection.commit();
        openChanges = false;
    }

    @Override
    public void commit() throws SQLException {
        //connection.commit();
    }

    @Override
    public void rollback() throws SQLException {
        LOG.info("rollback - {}", this);
        connection.rollback();
        openChanges = false;
    }

    public void closeIt() {
        LOG.info("closeIt - {}", this);
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DataSourceRuntimeException(e);
        }
        borrowed.remove(uuid);
        openChanges = false;
    }

    @Override
    public void close() throws SQLException {
        // throw new RuntimeException("NOOOOO!!!");
        //connection.close();
        //rollback();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return connection.getMetaData();
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return connection.isReadOnly();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        connection.setReadOnly(readOnly);
    }

    @Override
    public String getCatalog() throws SQLException {
        return connection.getCatalog();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        connection.setCatalog(catalog);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return connection.getTransactionIsolation();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        connection.setTransactionIsolation(level);
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return connection.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        connection.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        LOG.info("createStatement {} - {} - {}", this, resultSetType, resultSetConcurrency);
        openChanges = true;
        return connection.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        LOG.info("prepareStatement {} - {} -  {} - {}", this, sql, resultSetType, resultSetConcurrency);
        openChanges = true;
        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        LOG.info("prepareCall {} - {} - {} - {}", this, sql, resultSetType, resultSetConcurrency);
        openChanges = true;
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return connection.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        connection.setTypeMap(map);
    }

    @Override
    public int getHoldability() throws SQLException {
        return connection.getHoldability();
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        connection.setHoldability(holdability);
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return connection.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return connection.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        connection.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        connection.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        LOG.info("createStatement {} - {} -  {} - {}", this, resultSetType, resultSetConcurrency, resultSetHoldability);
        openChanges = true;
        return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        LOG.info("prepareStatement {} - {} -  {} - {} - {}", this, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        openChanges = true;
        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        LOG.info("prepareCall {} - {} - {} - {} - {}", this, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        openChanges = true;
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        LOG.info("prepareStatement {} - {} - {}", this, sql, autoGeneratedKeys);
        openChanges = true;
        return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        LOG.info("prepareStatement {} - {} - {}", this, sql, columnIndexes);
        openChanges = true;
        return connection.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        LOG.info("prepareStatement {} - {} - {}", this, sql, columnNames);
        openChanges = true;
        return connection.prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return connection.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return connection.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return connection.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return connection.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return connection.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        connection.setClientInfo(name, value);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return connection.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return connection.getClientInfo();
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        connection.setClientInfo(properties);
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return connection.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return connection.createStruct(typeName, attributes);
    }

    @Override
    public String getSchema() throws SQLException {
        return connection.getSchema();
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        connection.setSchema(schema);
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        connection.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        connection.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return connection.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return connection.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return connection.isWrapperFor(iface);
    }

    public void setBorrowed(Map<String, TestConnection> borrowed) {
        this.borrowed = borrowed;
    }

    @Override
    public String toString() {
        return
                this.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(this)) +
                        " - Connection@" + Integer.toHexString(System.identityHashCode(this.connection))
                ;
    }
}

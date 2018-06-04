package com.hrodberaht.inject.extensions.transaction.manager.impl.jdbc;

import com.hrodberaht.inject.extensions.transaction.junit.TransactionManagerTest;
import com.hrodberaht.inject.extensions.transaction.manager.impl.TransactionHolder;
import com.hrodberaht.inject.extensions.transaction.manager.impl.TransactionManagerBase;
import com.hrodberaht.inject.extensions.transaction.manager.impl.TransactionScopeHandler;
import com.hrodberaht.inject.extensions.transaction.manager.impl.jpa.StatisticsJPA;
import com.hrodberaht.inject.extensions.transaction.manager.internal.TransactionLogging;
import org.hrodberaht.injection.core.register.InjectionFactory;
import org.hrodberaht.injection.plugin.junit.datasource.DataSourceProxy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-21 20:29:06
 * @version 1.0
 * @since 1.0
 */
public class TransactionManagerJDBCImpl extends TransactionManagerBase<Connection>
        implements TransactionManagerJDBC,
        TransactionManagerTest,
        InjectionFactory<Connection> {

    protected static final TransactionScopeHandler entityManagerScope = new TransactionScopeHandler();
    protected static final ThreadLocal<Boolean> requiresNewDisabled = new ThreadLocal<Boolean>();

    private DataSource connectionFactory = null;
    private boolean isDataSourceProxy = false;

    public TransactionManagerJDBCImpl(DataSource connectionFactory) {
        this.connectionFactory = connectionFactory;
        try {
            if (connectionFactory instanceof DataSourceProxy) {
                isDataSourceProxy = true;
            }
        } catch (Throwable e) {
            TransactionLogging.log(e);
        }
    }

    public Connection getInstance() {
        return getNativeManager();
    }

    public Class getInstanceType() {
        return Connection.class;
    }

    public boolean newObjectOnInstance() {
        return false;
    }

    @Override
    protected void closeNative(Connection nativeTransaction) {
        try {
            if (isDataSourceProxy) {
                ((DataSourceProxy) connectionFactory).finalizeConnection(nativeTransaction);
            }
            nativeTransaction.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Connection createNativeManager() {
        try {
            if (isDataSourceProxy) {
                ((DataSourceProxy) connectionFactory).forceCreateNewConnection();
            }
            return connectionFactory.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected TransactionHolder<Connection> createTransactionHolder(TransactionHolder<Connection> connectionTransactionHolder) {
        return new ConnectionHolder(createNativeManager(), connectionTransactionHolder);
    }

    @Override
    protected TransactionHolder<Connection> createTransactionHolder() {
        return new ConnectionHolder(createNativeManager());
    }

    public void newBegin() {
        ConnectionHolder holder = (ConnectionHolder) findAndInitDeepestHolder();
        try {
            holder.getNativeManager().setAutoCommit(false);
            holder.setActive(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        TransactionLogging.log("TransactionManagerJDBCImpl: NewTx Begin for Connection {0}", holder.getNativeManager());
        StatisticsJPA.addBeginCount();
    }

    public void newCommit() {
        TransactionHolder<Connection> holder = findDeepestHolder();
        try {
            holder.getNativeManager().commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        TransactionLogging.log("TransactionManagerJDBCImpl: NewTx Commit for Connection {0}", holder.getNativeManager());
        StatisticsJPA.addCommitCount();
    }

    public void newRollback() {
        TransactionHolder<Connection> holder = findDeepestHolder();
        try {
            holder.getNativeManager().rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        TransactionLogging.log("TransactionManagerJDBCImpl: NewTx Rollback for Connection {0}", holder.getNativeManager());
        StatisticsJPA.addRollbackCount();
    }

    public boolean newIsActive() {
        ConnectionHolder holder = (ConnectionHolder) findDeepestHolder();
        if (holder != null
                && holder.isActive()) {
            return true;
        }
        return false;
    }

    public void newClose() {
        TransactionHolder<Connection> holder = findDeepestHolder();
        closeNative(holder.getNativeManager());
        TransactionLogging.log("TransactionManagerJDBCImpl: NewTx Close for Connection {0}", holder.getNativeManager());
        cleanupTransactionHolder(holder);
        StatisticsJPA.addCloseCount();
    }

    public void begin() {
        ConnectionHolder emh = (ConnectionHolder) findCreateManagerHolder();
        try {
            emh.getNativeManager().setAutoCommit(false);
            emh.setActive(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        TransactionLogging.log("TransactionManagerJDBCImpl: Tx Begin for Connection {0}", emh.getNativeManager());
        StatisticsJDBC.addBeginCount();

    }

    public void commit() {
        TransactionHolder<Connection> emh = findCreateManagerHolder();
        try {
            emh.getNativeManager().commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        TransactionLogging.log("TransactionManagerJDBCImpl: Tx Commit for Connection {0}", emh.getNativeManager());
        StatisticsJDBC.addCommitCount();
    }

    public void rollback() {
        TransactionHolder<Connection> emh = findCreateManagerHolder();
        try {
            emh.getNativeManager().rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        TransactionLogging.log("TransactionManagerJDBCImpl: Tx Rollback for Connection {0}", emh.getNativeManager());
        StatisticsJDBC.addRollbackCount();
    }

    public boolean isActive() {
        ConnectionHolder connectionHolder = getConnectionHolder();
        if (connectionHolder != null
                && connectionHolder.isActive()) {
            return true;
        }
        return false;
    }


    public void close() {

        ConnectionHolder holder = getConnectionHolder();
        if (isActive()) {
            closeNative(holder.getNativeManager());
            TransactionLogging.log("TransactionManagerJDBCImpl: Tx Close for Connection {0}", holder.getNativeManager());
            StatisticsJDBC.addCloseCount();
        }
        cleanupTransactionHolder(holder);
    }

    public boolean initTransactionHolder() {
        ConnectionHolder managerHolder = getConnectionHolder();
        if (managerHolder == null) {
            managerHolder = new ConnectionHolder();
            entityManagerScope.set(managerHolder);
            return true;
        }
        return false;
    }

    public void forceFlush() {
        // Think we do nothing for JDBC here.
    }

    @Override
    protected void postInitHolder(TransactionHolder<Connection> connectionTransactionHolder) {
        TransactionLogging.log("TransactionManagerJDBCImpl: On Demand Begin for Connection {0}"
                , connectionTransactionHolder.getNativeManager());
        ConnectionHolder connectionHolder = (ConnectionHolder) connectionTransactionHolder;
        connectionHolder.setActive(true);
    }

    public boolean requiresNewDisabled() {
        return requiresNewDisabled.get() != null;
    }

    @Override
    public void disableRequiresNew() {
        requiresNewDisabled.set(true);
    }

    @Override
    public void enableRequiresNew() {
        requiresNewDisabled.set(null);
    }

    @Override
    public TransactionScopeHandler getTransactionScopeHandler() {
        return entityManagerScope;
    }

    private ConnectionHolder getConnectionHolder() {
        ConnectionHolder connectionHolder = (ConnectionHolder) entityManagerScope.get();
        if (connectionHolder == null) {
            return null;
        }
        return (ConnectionHolder) connectionHolder.getCurrentActiveTransaction();
    }

}

package com.hrodberaht.inject.extensions.transaction.manager.impl.jpa;

import com.hrodberaht.inject.extensions.transaction.junit.TransactionManagerTest;
import com.hrodberaht.inject.extensions.transaction.manager.impl.TransactionHolder;
import com.hrodberaht.inject.extensions.transaction.manager.impl.TransactionManagerBase;
import com.hrodberaht.inject.extensions.transaction.manager.impl.TransactionScopeHandler;
import com.hrodberaht.inject.extensions.transaction.manager.internal.TransactionHandlingError;
import com.hrodberaht.inject.extensions.transaction.manager.internal.TransactionLogging;
import org.hrodberaht.injection.core.register.InjectionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-21 20:29:06
 * @version 1.0
 * @since 1.0
 */
public class TransactionManagerJPAImpl extends TransactionManagerBase<EntityManager>
        implements TransactionManagerJPA,
        TransactionManagerTest,
        InjectionFactory<EntityManager> {

    protected static final TransactionScopeHandler entityManagerThread = new TransactionScopeHandler();
    protected static final ThreadLocal<Boolean> requiresNewDisabled = new ThreadLocal<Boolean>();

    private EntityManagerFactory emf = null;

    public TransactionManagerJPAImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    protected EntityManager createNativeManager() {
        return emf.createEntityManager();
    }

    protected EntityManagerHolder createTransactionHolder(TransactionHolder<EntityManager> holder) {
        return new EntityManagerHolder(createNativeManager(), holder);
    }

    protected EntityManagerHolder createTransactionHolder() {
        return new EntityManagerHolder(createNativeManager());
    }

    @Override
    protected void postInitHolder(TransactionHolder<EntityManager> entityManagerTransactionHolder) {
        // do nothing here as JPA has active transaction as part of its internal support
        TransactionLogging.log("TransactionManagerJPAImpl: On Demand Begin for session {0}"
                , entityManagerTransactionHolder.getNativeManager());
    }

    public void begin() {
        TransactionHolder<EntityManager> emh = findCreateManagerHolder();
        verifyTransactionHolder(emh);
        if (emh.getNativeManager().getTransaction().isActive()) {
            throw new IllegalStateException("Transaction already active");
        }
        emh.getNativeManager().getTransaction().begin();
        TransactionLogging.log("TransactionManagerJPAImpl: Tx Begin for session {0}", emh.getNativeManager());
        StatisticsJPA.addBeginCount();
    }

    public void commit() {
        TransactionHolder<EntityManager> emh = findCreateManagerHolder();
        verifyTransactionHolder(emh);
        EntityManager em = emh.getNativeManager();
        em.getTransaction().commit();
        TransactionLogging.log("TransactionManagerJPAImpl: Tx Commit for session {0}", em);
        StatisticsJPA.addCommitCount();
    }

    private void verifyTransactionHolder(TransactionHolder emh) {
        if (emh.isRollbackOnly()) {
            throw new TransactionHandlingError("Transaction is marked for rollback only");
        }
    }

    public void rollback() {
        TransactionHolder<EntityManager> emh = findCreateManagerHolder();
        EntityManager em = emh.getNativeManager();
        rollbackEntityManager(em);
        emh.setRollbackOnly(true);
        TransactionLogging.log("TransactionManagerJPAImpl: Tx Rollback for session {0}", em);
        StatisticsJPA.addRollbackCount();
    }

    private void rollbackEntityManager(EntityManager em) {
        em.getTransaction().rollback();
        em.clear();
    }

    public boolean isActive() {
        TransactionHolder<EntityManager> emh = getEntityManagerScope();
        if (emh == null) {
            return false;
        }
        EntityManager em = emh.getNativeManager();
        if (em == null) {
            return false;
        }
        return em.getTransaction().isActive();
    }

    public void close() {
        if (!isClosed()) {
            TransactionHolder<EntityManager> emh = getEntityManagerScope();
            TransactionLogging.log("TransactionManagerJPAImpl: Tx Close for session {0}", emh.getNativeManager());
            closeEntityManager(emh.getNativeManager());
            emh.setNativeManager(null);
            entityManagerThread.set(null);
            StatisticsJPA.addCloseCount();
        }
    }

    private void closeEntityManager(EntityManager em) {
        if (em != null) {
            //em.flush();
            //em.clear();
            em.close();
        }
    }

    public boolean initTransactionHolder() {
        TransactionHolder<EntityManager> managerHolder = getEntityManagerScope();
        if (managerHolder == null) {
            managerHolder = new EntityManagerHolder();
            entityManagerThread.set(managerHolder);
            return true;
        }
        return false;
    }

    private boolean isClosed() {
        TransactionHolder<EntityManager> managerHolder = getEntityManagerScope();
        if (managerHolder != null) {
            return false;
        }
        return true;
    }


    public void newBegin() {
        TransactionHolder<EntityManager> holder = findAndInitDeepestHolder();
        holder.getNativeManager().getTransaction().begin();
        TransactionLogging.log("TransactionManagerJPAImpl: NewTx Begin for session {0}", holder.getNativeManager());
        StatisticsJPA.addBeginCount();

    }

    public void newCommit() {
        TransactionHolder<EntityManager> holder = findDeepestHolder();
        holder.getNativeManager().getTransaction().commit();
        // cleanupTransactionHolder(holder);
        TransactionLogging.log("TransactionManagerJPAImpl: NewTx Commit for session {0}", holder.getNativeManager());
        StatisticsJPA.addCommitCount();
    }

    public void newRollback() {
        TransactionHolder<EntityManager> holder = findDeepestHolder();
        rollbackEntityManager(holder.getNativeManager());
        // cleanupTransactionHolder(holder);
        TransactionLogging.log("TransactionManagerJPAImpl: NewTx Rollback for session {0}", holder.getNativeManager());
        StatisticsJPA.addRollbackCount();
    }

    public void newClose() {
        TransactionHolder<EntityManager> holder = findDeepestHolder();
        if (holder != null) {
            TransactionLogging.log("TransactionManagerJPAImpl: NewTX closing session {0}", holder.getNativeManager());
            closeEntityManager(holder.getNativeManager());
            cleanupTransactionHolder(holder);
            StatisticsJPA.addCloseCount();
        } else {
            TransactionLogging.log("TransactionManagerJPAImpl: NewTX NOT closing due to null");
        }
    }

    public boolean newIsActive() {
        TransactionHolder<EntityManager> emh = findDeepestHolder();
        if (emh == null) {
            return false;
        }
        EntityManager em = emh.getNativeManager();
        if (em == null) {
            return false;
        }
        return em.getTransaction().isActive();
    }


    private EntityManagerHolder getEntityManagerScope() {
        return (EntityManagerHolder) entityManagerThread.get();
    }

    public void forceFlush() {
        EntityManagerHolder holder = getEntityManagerScope();
        holder.getNativeManager().flush();
        holder.getNativeManager().clear();
    }

    public EntityManager getInstance() {
        return getNativeManager();
    }

    public Class getInstanceType() {
        return EntityManager.class;
    }

    public boolean newObjectOnInstance() {
        return false;
    }

    @Override
    public void closeNative(EntityManager nativeTransaction) {
        nativeTransaction.close();
    }

    public boolean requiresNewDisabled() {
        return requiresNewDisabled.get() != null;
    }

    public void disableRequiresNew() {
        requiresNewDisabled.set(true);
    }

    public void enableRequiresNew() {
        requiresNewDisabled.remove();
    }

    @Override
    public TransactionScopeHandler getTransactionScopeHandler() {
        return entityManagerThread;
    }
}

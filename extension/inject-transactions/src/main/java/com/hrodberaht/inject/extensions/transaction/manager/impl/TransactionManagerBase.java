package com.hrodberaht.inject.extensions.transaction.manager.impl;

import com.hrodberaht.inject.extensions.transaction.TransactionManager;
import com.hrodberaht.inject.extensions.transaction.manager.RequiresNewTransactionManager;
import com.hrodberaht.inject.extensions.transaction.manager.internal.TransactionHandlingError;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-21 20:29:06
 * @version 1.0
 * @since 1.0
 */
public abstract class TransactionManagerBase<T> implements TransactionManager, RequiresNewTransactionManager {

    protected abstract void closeNative(T nativeTransaction);

    protected abstract T createNativeManager();

    protected abstract TransactionHolder<T> createTransactionHolder(TransactionHolder<T> holder);

    protected abstract TransactionHolder<T> createTransactionHolder();

    protected abstract void postInitHolder(TransactionHolder<T> holder);

    public abstract void disableRequiresNew();

    public abstract void enableRequiresNew();

    public abstract TransactionScopeHandler<T> getTransactionScopeHandler();


    protected void cleanupTransactionHolder(TransactionHolder<T> holder) {
        holder.setNativeManager(null);
        if (holder.getParentTransaction() == null) {
            getTransactionScopeHandler().set(null);
        } else {
            // cleanup self reference
            holder.getParentTransaction().setChildTransaction(null);
            getTransactionScopeHandler().get().setCurrentActiveTransaction(holder.getParentTransaction());
        }
        closeAllChildren(holder);
    }

    private void closeAllChildren(TransactionHolder<T> holder) {
        if (holder.getChildTransaction() != null) {
            closeNative(holder.getChildTransaction().getNativeManager());
            holder.getChildTransaction().setNativeManager(null);
            holder.getChildTransaction().setParentTransaction(null);
            closeAllChildren(holder.getChildTransaction());
        }
    }

    protected TransactionHolder<T> findCreateManagerHolder() {
        TransactionHolder<T> manager = getTransactionScopeHandler().get();
        if (manager == null) {
            manager = createTransactionHolder();
            getTransactionScopeHandler().set(manager);
        } else if (manager.isNew) {
            manager.isNew = false;
        }
        return manager.getCurrentActiveTransaction();
    }


    public T getNativeManager() {
        TransactionHolder<T> holder = findDeepestHolder();
        if (holder == null) {
            // This can happen when NOT_SUPPORTED tries to lookup a Manager
            throw new TransactionHandlingError("The transaction holder has not been initialized");
        } else if (holder.getNativeManager() == null) {
            // Late create of manager (for SUPPORTS)
            holder.setNativeManager(createNativeManager());
            postInitHolder(holder);
        }
        return holder.getNativeManager();
    }

    protected TransactionHolder<T> findAndInitDeepestHolder() {
        TransactionHolder<T> holder = getTransactionScopeHandler().get();
        TransactionHolder<T> baseholder = holder;
        if (holder == null) {
            // If first transaction is REW_NEW, just do as normal.
            holder = createTransactionHolder();
            getTransactionScopeHandler().set(holder);
            return holder;
        }
        while (holder.getChildTransaction() != null) {
            holder = holder.getChildTransaction();
        }
        // Deepest holder is found
        TransactionHolder<T> newHolder = createTransactionHolder(holder);
        holder.setChildTransaction(newHolder);
        baseholder.setCurrentActiveTransaction(newHolder);
        return newHolder;
    }


    protected TransactionHolder<T> findDeepestHolder() {
        TransactionHolder<T> holder = getTransactionScopeHandler().get();
        if (holder == null) {
            return null;
        }
        return holder.getCurrentActiveTransaction();        
    }

}

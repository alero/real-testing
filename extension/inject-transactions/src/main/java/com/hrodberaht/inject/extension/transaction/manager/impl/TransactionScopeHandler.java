package com.hrodberaht.inject.extension.transaction.manager.impl;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-21 20:29:06
 * @version 1.0
 * @since 1.0
 */

public class TransactionScopeHandler<T> {
    private final InheritableThreadLocal<TransactionHolder> entityManagerScope =
            new InheritableThreadLocal<TransactionHolder>();

    public TransactionHolder<T> get() {
        return entityManagerScope.get();
    }

    public void set(TransactionHolder<T> data) {
        entityManagerScope.set(data);
    }


}
package com.hrodberaht.inject.extension.transaction.manager.impl.jdbc;

import com.hrodberaht.inject.extension.transaction.manager.impl.TransactionHolder;

import java.sql.Connection;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-21 20:29:06
 * @version 1.0
 * @since 1.0
 */
public class ConnectionHolder extends TransactionHolder<Connection> {

    private boolean active = false;

    public ConnectionHolder() {
        super.currentActiveTransaction = this;
    }

    public ConnectionHolder(Connection entityManager) {
        super.nativeManager = entityManager;
        super.currentActiveTransaction = this;
    }

    public ConnectionHolder(Connection entityManager, TransactionHolder<Connection> holder) {
        this.nativeManager = entityManager;
        this.parentTransaction = holder;        
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

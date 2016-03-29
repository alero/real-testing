package com.hrodberaht.inject.extensions.transaction.manager.impl.jpa;

import com.hrodberaht.inject.extensions.transaction.manager.impl.TransactionHolder;

import javax.persistence.EntityManager;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-21 20:29:06
 * @version 1.0
 * @since 1.0
 */
public class EntityManagerHolder extends TransactionHolder<EntityManager> {
    public EntityManagerHolder() {
        super.currentActiveTransaction = this;
    }

    public EntityManagerHolder(EntityManager entityManager) {
        super.nativeManager = entityManager;
        super.currentActiveTransaction = this;
    }

    public EntityManagerHolder(EntityManager entityManager, TransactionHolder<EntityManager> holder) {
        this.nativeManager = entityManager;
        this.parentTransaction = holder;        
    }


}

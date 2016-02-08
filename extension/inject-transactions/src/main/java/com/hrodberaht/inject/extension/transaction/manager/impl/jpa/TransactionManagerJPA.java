package com.hrodberaht.inject.extension.transaction.manager.impl.jpa;

import com.hrodberaht.inject.extension.transaction.TransactionManager;

import javax.persistence.EntityManager;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-21 20:29:06
 * @version 1.0
 * @since 1.0
 */
public interface TransactionManagerJPA extends TransactionManager {
    EntityManager getNativeManager();    
}

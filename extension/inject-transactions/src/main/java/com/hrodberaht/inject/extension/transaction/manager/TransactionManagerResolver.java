package com.hrodberaht.inject.extension.transaction.manager;

import com.hrodberaht.inject.extension.transaction.TransactionManager;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-25 23:17:22
 * @version 1.0
 * @since 1.0
 */
public interface TransactionManagerResolver {

    TransactionManager getTransactionManager();

}

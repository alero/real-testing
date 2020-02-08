package com.hrodberaht.inject.extensions.transaction;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-06 18:31:10
 * @version 1.0
 * @since 1.0
 */
public interface TransactionManager {

    void begin();

    void commit();

    void rollback();

    boolean isActive();

    void close();

    boolean initTransactionHolder();
}

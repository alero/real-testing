package com.hrodberaht.inject.extensions.transaction.junit;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-11 22:58:13
 * @version 1.0
 * @since 1.0
 */
public interface TransactionManagerTest {
    void forceFlush();

    void disableRequiresNew();

    void enableRequiresNew();
}

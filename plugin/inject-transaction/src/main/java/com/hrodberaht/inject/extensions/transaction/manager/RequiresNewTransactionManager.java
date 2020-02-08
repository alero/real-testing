package com.hrodberaht.inject.extensions.transaction.manager;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-11 22:58:13
 * @version 1.0
 * @since 1.0
 */
public interface RequiresNewTransactionManager {
    void newBegin();

    void newCommit();

    void newRollback();

    boolean newIsActive();

    void newClose();

    boolean requiresNewDisabled();
}

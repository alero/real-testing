package com.hrodberaht.inject.extension.transaction.manager.internal;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-11 23:16:00
 * @version 1.0
 * @since 1.0
 */
public class TransactionHandlingError extends RuntimeException{

    public TransactionHandlingError(String message) {
        super(message);
    }
}

package com.hrodberaht.inject.extensions.transaction.manager.internal;

import com.hrodberaht.inject.extensions.transaction.TransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-11 22:58:13
 * @version 1.0
 * @since 1.0
 */
public class TransactionMandatory {

    public TransactionMandatory() {
    }

    Object transactionHandling(ProceedingJoinPoint thisJoinPoint, TransactionManager transactionManager) throws Throwable {
        if (!transactionManager.isActive()) {
            throw new TransactionHandlingError("has no active transaction");
        }
        return thisJoinPoint.proceed();
    }


}
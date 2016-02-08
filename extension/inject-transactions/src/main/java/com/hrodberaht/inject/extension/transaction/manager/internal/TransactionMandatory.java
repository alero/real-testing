package com.hrodberaht.inject.extension.transaction.manager.internal;

import com.hrodberaht.inject.extension.transaction.TransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-11 22:58:13
 * @version 1.0
 * @since 1.0
 */
public class TransactionMandatory {

    public TransactionMandatory() {
    }

    Object transactionHandling(ProceedingJoinPoint thisJoinPoint, TransactionManager transactionManager) throws Throwable {        
        if (!transactionManager.isActive()) {
            System.out.println("Mandatory error");
            throw new TransactionHandlingError("has no active transaction");
        }
        System.out.println("Mandatory ok");
        return thisJoinPoint.proceed();
    }

    

}
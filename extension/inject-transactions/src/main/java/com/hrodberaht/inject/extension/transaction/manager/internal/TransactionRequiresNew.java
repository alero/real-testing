package com.hrodberaht.inject.extension.transaction.manager.internal;

import com.hrodberaht.inject.extension.transaction.TransactionManager;
import com.hrodberaht.inject.extension.transaction.manager.RequiresNewTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-11 22:58:13
 * @version 1.0
 * @since 1.0
 */
public class TransactionRequiresNew {

    public TransactionRequiresNew() {
    }

    Object transactionHandling(ProceedingJoinPoint thisJoinPoint, TransactionManager transactionManager)
            throws Throwable {


        if(!(transactionManager instanceof RequiresNewTransactionManager)){
            throw new IllegalAccessError("transaction manager does not support requires new: "
                    +transactionManager.getClass());
        }
        RequiresNewTransactionManager newTransactionManager = (RequiresNewTransactionManager)transactionManager;
        if(newTransactionManager.requiresNewDisabled()){
            return new TransactionRequired().transactionHandling(thisJoinPoint, transactionManager);        
        }

        try {
            TransactionLogging.log("TransactionRequiresNew: Begin Transactional call : {0}",
                    thisJoinPoint.getSignature().getName());
            newTransactionManager.newBegin();

            Object proceed = thisJoinPoint.proceed();

            TransactionLogging.log("TransactionRequiresNew: Commit/Close Transactional call : {0}",
                    thisJoinPoint.getSignature().getName());
            if(newTransactionManager.newIsActive()){
                newTransactionManager.newCommit();
            }

            return proceed;
        } catch (Throwable error) {
            TransactionLogging.log("TransactionRequiresNew: Error Transactional call : {0}",
                    thisJoinPoint.getSignature().getName());
            if (newTransactionManager.newIsActive()) {
                newTransactionManager.newRollback();
            }
            throw error;
        } finally{
            // if (newTransactionManager.newIsActive()) {
            newTransactionManager.newClose();
            // }
        }
    }

    

}
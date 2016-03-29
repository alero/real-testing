package com.hrodberaht.inject.extensions.transaction.manager.internal;

import com.hrodberaht.inject.extensions.transaction.TransactionManager;
import org.aspectj.lang.Aspects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-10 18:47:12
 * @version 1.0
 * @since 1.0
 */
@Aspect
public class AspectJTransactionHandler extends Aspects {

    private static AspectJTransactionHandler singleton = new AspectJTransactionHandler();


    public AspectJTransactionHandler() {
        singleton = this;
    }

    public static AspectJTransactionHandler aspectOf() {
        if (singleton == null) {
            singleton = new AspectJTransactionHandler();
        }
        return singleton;
    }

    @Inject
    private Provider<TransactionManager> transactionManagerProvider;


    @Pointcut("execution(@javax.ejb.TransactionAttribute * *(..)) && @annotation(transactionAttribute)")
    public void transactionalPointCut(TransactionAttribute transactionAttribute) {

    }

    @Around("transactionalPointCut(transactionAttribute)")
    public Object transactional(ProceedingJoinPoint thisJoinPoint, TransactionAttribute transactionAttribute)
            throws Throwable {

        TransactionAttributeType transactionAttributeType = findTransactionType(transactionAttribute);
        TransactionLogging.log("transactional begin {0}", transactionAttributeType.name());


        TransactionManager transactionManager = transactionManagerProvider.get();
        if (transactionManager == null) {
            throw new TransactionHandlingError("transactionManager is null");
        }
        if (transactionAttributeType == TransactionAttributeType.REQUIRED) {
            return new TransactionRequired().transactionHandling(thisJoinPoint, transactionManager);
        } else if (transactionAttributeType == TransactionAttributeType.SUPPORTS) {
            return new TransactionSupports().transactionHandling(thisJoinPoint, transactionManager);
        } else if (transactionAttributeType == TransactionAttributeType.REQUIRES_NEW) {
            return new TransactionRequiresNew().transactionHandling(thisJoinPoint, transactionManager);
        } else if (transactionAttributeType == TransactionAttributeType.MANDATORY) {
            return new TransactionMandatory().transactionHandling(thisJoinPoint, transactionManager);
        } else if (transactionAttributeType == TransactionAttributeType.NOT_SUPPORTED) {
            return new TransactionNotSupported().transactionHandling(thisJoinPoint, transactionManager);
        }
        // If nothing found
        throw new TransactionHandlingError(
                "transactionManager has no supported transactionAttributeType: " + transactionAttributeType
        );

    }


    private TransactionAttributeType findTransactionType(TransactionAttribute transactionAttribute) {
        TransactionAttributeType transactionAttributeType = null;
        if (transactionAttribute == null) {
            TransactionLogging.log("Annotation not found ");
            transactionAttributeType = TransactionAttributeType.REQUIRED;
        } else {
            transactionAttributeType = transactionAttribute.value();
        }
        return transactionAttributeType;
    }


}

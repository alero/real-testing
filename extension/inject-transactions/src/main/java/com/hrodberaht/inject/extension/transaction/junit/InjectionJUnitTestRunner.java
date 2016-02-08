package com.hrodberaht.inject.extension.transaction.junit;

import com.hrodberaht.inject.extension.transaction.TransactionManager;
import com.hrodberaht.inject.extension.transaction.manager.internal.TransactionLogging;
import com.hrodberaht.inject.extension.transaction.manager.util.TransactionManagerUtil;
import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.internal.exception.InjectRuntimeException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import javax.ejb.TransactionAttribute;
import java.lang.annotation.Annotation;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-10 20:29:06
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        TransactionAttribute will enable the class or a method to be "TestCase-Transactional"
 *        The no need for a type, they will all be REQUIRED with rollback as the end call.
 *        The BEGIN/ROLLBACK will always be enforced if the test is classified as "TestCase-Transactional"
 *        <p/>
 *        TIP1: To make an entire class transactional use @TransactionAttribute as a class type annotation
 *        TIP1a: If the class is transactional, to disable a single test to be non transactional
 *        use the @TransactionDisabled annotation for that method.
 *        <p/>
 *        TIP2: To enable transaction support for a single method just use the @TransactionAttribute for that method.
 */
public class InjectionJUnitTestRunner extends BlockJUnit4ClassRunner {

    private InjectContainer theContainer = null;
    private TransactionManager transactionManager = null;
    private boolean allMethodsTransacted = false;
    private boolean disableRequiresNewTransaction = false;

    private boolean transactionManagerIsPresent = false;

    /**
     * Creates a BlockJUnit4ClassRunner to run {@code klass}
     *
     * @throws org.junit.runners.model.InitializationError
     *          if the test class is malformed.
     */
    public InjectionJUnitTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
        createContainerFromRegistration();
    }

    private void createContainerFromRegistration() {
        try {
            Class testClass = getTestClass().getJavaClass();
            Annotation[] annotations = testClass.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == InjectionContainerContext.class) {
                    InjectionContainerContext transactionContainer = (InjectionContainerContext) annotation;
                    Class<? extends InjectionContainerCreator> transactionClass = transactionContainer.value();
                    InjectionContainerCreator creator = transactionClass.newInstance();
                    theContainer = creator.createContainer();
                    if(TransactionManagedTesting.class.isAssignableFrom(creator.getClass())) {
                        verifyContainerTransactions(theContainer, creator);
                        transactionManagerIsPresent = true;
                    }

                    disableRequiresNewTransaction = transactionContainer.disableRequiresNewTransaction();
                }
                if (annotation.annotationType() == TransactionAttribute.class) {
                    allMethodsTransacted = true;
                }
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Each test is verified for transaction support.
     * To enable a single methods for transaction use the @TransactionAttribute
     * Disabled TransactionDisabled comes first and will always disable (even if a TransactionAttribute exists)
     *
     * @param frameworkMethod
     * @param notifier
     * @see TransactionDisabled
     * @see TransactionAttribute
     */
    @Override
    protected void runChild(FrameworkMethod frameworkMethod, RunNotifier notifier) {
        boolean hasTransaction = hasTransaction(frameworkMethod);
        // Need to re-init the TransactionAspect (Junit and AspectJ does not play well)
        if(transactionManagerIsPresent) {
            injectTransactionHandler();
        }

        try {
            TransactionLogging.log ("---  InjectionJUnitTestRunner: " +
                    " running child {0} in thread {1}",frameworkMethod.getName(), Thread.currentThread());
            if (disableRequiresNewTransaction) {
                ((TransactionManagerTest) transactionManager).disableRequiresNew();
            }
            boolean transactionNew = false;

            if (hasTransaction && transactionManagerIsPresent) {
                if (transactionManager.isActive()) {
                    throw new RuntimeException("Transaction is already active");
                }
                transactionNew = true;
                transactionManager.begin();
            }

            try {
                super.runChild(frameworkMethod, notifier);
            } finally {
                if (hasTransaction && transactionManagerIsPresent) {
                    if (transactionManager.isActive()) {
                        ((TransactionManagerTest) transactionManager).forceFlush();
                        transactionManager.rollback();
                    }
                    if (transactionNew) {
                        transactionManager.close();
                    }
                }
                if (disableRequiresNewTransaction) {
                    ((TransactionManagerTest) transactionManager).enableRequiresNew();
                }
            }
        } catch (Throwable e) {
            Description description = describeChild(frameworkMethod);
            notifier.fireTestFailure(new Failure(description, e));
            notifier.fireTestFinished(description);
        }
        TransactionLogging.log("---  InjectionJUnitTestRunner: " +
                " done running child {0} in thread {1}",frameworkMethod.getName(), Thread.currentThread());
    }

    private void injectTransactionHandler() {
        if(System.getProperty("inject.transactionManager.junitregistration") == null){
            try{
                TransactionManagerUtil.registerTransactionManager(theContainer);
            }catch (InjectRuntimeException ignore){
                // don't care if the transaction manager is not registered
            }
        }
    }

    /**
     * Delegates to the parent implementation for creating the test instance and
     * then allows the container to prepare the test instance before returning it.
     */
    @Override
    protected Object createTest() throws Exception {
        Object testInstance = super.createTest();
        theContainer.injectDependencies(testInstance);
        return testInstance;
    }


    private boolean hasTransaction(FrameworkMethod frameworkMethod) {
        return !hasMethodTransactionDisabled(frameworkMethod) &&
                (allMethodsTransacted || hasMethodTransactionAnnotation(frameworkMethod));
    }

    private boolean hasMethodTransactionDisabled(FrameworkMethod frameworkMethod) {
        if (frameworkMethod.getAnnotation(TransactionDisabled.class) != null) {
            return true;
        }
        return false;
    }

    private boolean hasMethodTransactionAnnotation(FrameworkMethod frameworkMethod) {
        if (frameworkMethod.getAnnotation(TransactionAttribute.class) != null) {
            return true;
        }
        return false;
    }

    private void verifyContainerTransactions(InjectContainer theContainer, com.hrodberaht.inject.extension.transaction.junit.InjectionContainerCreator creator) {
        try {
            transactionManager = theContainer.get(TransactionManager.class);
            TransactionLogging.log(
                    "TransactionManager ({0}) " +
                    "successfully wired from creator: {1}", transactionManager.getClass().getSimpleName(), creator.getClass().getSimpleName());

        } catch (InjectRuntimeException exception) {
            TransactionLogging.log("InjectionJUnitTestRunner: " +
                    "TransactionManager not wired for Container from creator: {0}",creator.getClass().getName());
            exception.printStackTrace(System.err);
        }
    }
}

package org.hrodberaht.injection.extensions.junit;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.junit.internal.TransactionManager;
import org.hrodberaht.injection.spi.ContainerConfig;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.lang.annotation.Annotation;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:32:34
 * @version 1.0
 * @since 1.0
 */
public class JUnitRunner extends BlockJUnit4ClassRunner {

    private InjectContainer activeContainer = null;

    private ContainerConfig creator = null;

    /**
     * Creates a BlockJUnit4ClassRunner to run
     *
     * @throws org.junit.runners.model.InitializationError if the test class is malformed.
     */
    public JUnitRunner(Class<?> klass) throws InitializationError {
        super(klass);
        createContainerFromRegistration();
    }

    private void createContainerFromRegistration() {
        try {
            Class testClass = getTestClass().getJavaClass();
            Annotation[] annotations = testClass.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == ContainerContext.class) {
                    ContainerContext containerContext = (ContainerContext) annotation;
                    Class<? extends ContainerConfig> transactionClass = containerContext.value();
                    creator = transactionClass.newInstance();
                    creator.createContainer();
                    System.out.println("Creating creator for thread " + Thread.currentThread().toString());
                }
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param frameworkMethod
     * @param notifier
     */
    @Override
    protected void runChild(FrameworkMethod frameworkMethod, RunNotifier notifier) {
        try {

            TransactionManager.beginTransaction(creator);

            // So that ContainerLifeCycleTestUtil can access the activeContainer and do magic
            creator.addSingletonActiveRegistry();

            activeContainer = creator.getActiveRegister().getContainer();
            try {
                // This will execute the createTest method below, the activeContainer handling relies on this.
                System.out.println("START running test " +
                        frameworkMethod.getName() + " for thread " + Thread.currentThread().toString());
                super.runChild(frameworkMethod, notifier);
                System.out.println("END running test " +
                        frameworkMethod.getName() + " for thread " + Thread.currentThread().toString());
            } finally {
                TransactionManager.endTransaction();
                creator.cleanActiveContainer();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Description description = describeChild(frameworkMethod);
            notifier.fireTestFailure(new Failure(description, e));
            notifier.fireTestFinished(description);
        }
    }

    /**
     * Runs the injection of dependencies and resources on the test case before returned
     *
     * @return the testcase
     * @throws Exception
     */
    @Override
    protected Object createTest() throws Exception {
        Object testInstance = super.createTest();
        // The active container will automatically injectMethod all normal dependencies and resources
        activeContainer.injectDependencies(testInstance);
        return testInstance;
    }

}

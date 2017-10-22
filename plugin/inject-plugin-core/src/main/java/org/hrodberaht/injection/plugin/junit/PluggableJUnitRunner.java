package org.hrodberaht.injection.plugin.junit;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.plugin.junit.inner.RunnerPlugins;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:32:34
 * @version 1.0
 * @since 1.0
 */
public class PluggableJUnitRunner extends BlockJUnit4ClassRunner {

    private static final Logger LOG = LoggerFactory.getLogger(PluggableJUnitRunner.class);
    private InjectContainer activeContainer = null;
    private PluggableContainerConfigBase containerConfig = null;
    private RunnerPlugins runnerPlugins = null;

    /**
     * Creates a BlockJUnit4ClassRunner to run
     *
     * @throws InitializationError if the test class is malformed.
     */
    public PluggableJUnitRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
        createContainerFromRegistration();
    }

    private void createContainerFromRegistration() {
        try {
            Class testClass = getTestClass().getJavaClass();
            Annotation[] annotations = testClass.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == ContainerContext.class) {
                    createUnitTestContext((ContainerContext) annotation);
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InjectRuntimeException(e);
        }
    }

    private void createUnitTestContext(ContainerContext annotation) throws InstantiationException, IllegalAccessException {
        ContainerContext containerContext = annotation;
        Class testConfigClass = containerContext.value();
        if (PluggableContainerConfigBase.class.isAssignableFrom(testConfigClass)) {
            containerConfig = (PluggableContainerConfigBase) testConfigClass.newInstance();
            runnerPlugins = getRunnerPlugins();
            runnerPlugins.runInitBeforeContainer();
            containerConfig.start();
            runnerPlugins.runInitAfterContainer(containerConfig.getActiveRegister());

            LOG.info("Creating creator for thread {}", Thread.currentThread().getName());
        } else {
            throw new IllegalAccessError("Currently the test config class must extrend PluggableContainerConfigBase");
        }
    }

    private RunnerPlugins getRunnerPlugins() {
        if (containerConfig != null) {
            return containerConfig.getRunnerPlugins();
        } else {
            return new RunnerPlugins();
        }
    }

    /**
     * @param frameworkMethod
     * @param notifier
     */
    @Override
    protected void runChild(FrameworkMethod frameworkMethod, RunNotifier notifier) {
        try {

            beforeRunChild();
            try {
                // This will execute the createTest method below, the activeContainer handling relies on this.
                LOG.info("START running test " +
                        frameworkMethod.getName() + " for thread " + Thread.currentThread().getName());
                super.runChild(frameworkMethod, notifier);
                LOG.info("END running test " +
                        frameworkMethod.getName() + " for thread " + Thread.currentThread().getName());
            } finally {
                afterRunChild();
            }
        } catch (Throwable e) {
            LOG.error("Fatal test error :" + frameworkMethod.getName(), e);
            Description description = describeChild(frameworkMethod);
            notifier.fireTestFailure(new Failure(description, e));
            notifier.fireTestFinished(description);
        }
    }

    private void beforeRunChild() {
        runnerPlugins.runBeforeTest(containerConfig.getActiveRegister());
        // TransactionManager.beginTransaction(creator);

        // So that ContainerLifeCycleTestUtil can access the activeContainer and do magic
        containerConfig.beforeRunChild();

        activeContainer = containerConfig.getActiveRegister().getContainer();
    }

    private void afterRunChild() {
        runnerPlugins.runAfterTest(containerConfig.getActiveRegister());
        // TransactionManager.endTransaction();
        containerConfig.cleanActiveContainer();
    }

    @Override
    public void run(RunNotifier notifier) {
        runnerPlugins.runBeforeTestClass(containerConfig.getActiveRegister());
        super.run(notifier);
        runnerPlugins.runAfterTestClass(containerConfig.getActiveRegister());
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
        activeContainer.autowireAndPostConstruct(testInstance);
        return testInstance;
    }

}

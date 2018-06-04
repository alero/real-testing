package org.hrodberaht.injection.plugin.junit;

import org.hrodberaht.injection.core.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.plugin.junit.spring.beans.SpringEntityManager;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpringJUnit4Runner extends SpringJUnit4ClassRunner {

    private static final Logger LOG = LoggerFactory.getLogger(SpringJUnit4Runner.class);

    private final JUnitContext jUnitContext;
    private TestContext testContext;
    private TransactionalTestExecutionListener transactionalTestExecutionListener;

    /**
     * Creates a BlockJUnit4ClassRunner to run
     *
     * @param clazz
     * @throws InitializationError if the test class is malformed.
     */
    public SpringJUnit4Runner(Class<?> clazz) throws InitializationError {
        super(clazz);
        jUnitContext = new JUnitContext(clazz) {
            @Override
            void runBeforeTest(boolean activateContainer, String testName) {
                super.runBeforeTest(activateContainer, testName);
            }
        };
    }

    @Override
    protected void runChild(FrameworkMethod frameworkMethod, RunNotifier notifier) {
        jUnitContext.runChild(frameworkMethod, () -> super.runChild(frameworkMethod, notifier), (e) -> {
            Description description = describeChild(frameworkMethod);
            notifier.fireTestFailure(new Failure(description, e));
            notifier.fireTestFinished(description);
        });
    }

    private void flushEntityManager() {
        SpringEntityManager springEntityManager = getSpringEntityManager();
        if (springEntityManager != null) {
            if (springEntityManager.getEntityManager() != null) {
                try {
                    springEntityManager.getEntityManager().flush();
                    // springEntityManager.getEntityManager().close();
                } catch (RuntimeException exception) {
                    // No not fail due to springEntityManager issues
                }

            }
        }

    }

    private SpringEntityManager getSpringEntityManager() {
        try {
            return jUnitContext.get(ApplicationContext.class)
                    .getBean(SpringEntityManager.class);
        } catch (Exception ex) {
            LOG.debug("SpringJUnitRunner info: " + ex.getMessage());
            return null;
        }
    }

    @Override
    protected Object createTest() throws Exception {
        Object testInstance = super.createTest();
        jUnitContext.autoWireTestObject(testInstance);
        return testInstance;
    }

    @Override
    protected TestContextManager createTestContextManager(Class<?> clazz) {
        TestContextManager contextManager = new TestContextManagerLocal(clazz);
        replaceTestContext(contextManager);
        return contextManager;
    }

    private void replaceTestContext(TestContextManager contextManager) {
        try {
            Field field = TestContextManager.class.getDeclaredField("testContext");
            field.setAccessible(true);
            testContext = (TestContext) field.get(contextManager);
            field.set(contextManager, new TestContextLocal(testContext));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new InjectRuntimeException(e);
        }
    }


    private class TestContextManagerLocal extends TestContextManager {

        public TestContextManagerLocal(Class<?> testClass) {
            super(testClass);
        }

        @Override
        public void registerTestExecutionListeners(List<TestExecutionListener> testExecutionListeners) {


            final List<TestExecutionListener> listeners = new ArrayList<>();
            testExecutionListeners
                    .stream()
                    .filter(t -> !(t instanceof DependencyInjectionTestExecutionListener))
                    .collect(Collectors.toList())
                    .forEach(t -> {
                                if (t instanceof TransactionalTestExecutionListener) {
                                    transactionalTestExecutionListener = new TransactionalTestExecutionListener() {
                                        @Override
                                        public void afterTestMethod(TestContext testContext) throws Exception {
                                            flushEntityManager();
                                            super.afterTestMethod(testContext);
                                        }
                                    };
                                    listeners.add(transactionalTestExecutionListener);
                                } else {
                                    listeners.add(t);
                                }
                            }

                    );

            super.registerTestExecutionListeners(listeners);
        }
    }

    private class TestContextLocal implements TestContext {
        private TestContext testContext;

        public TestContextLocal(TestContext testContext) {
            this.testContext = testContext;
        }

        @Override
        public ApplicationContext getApplicationContext() {
            return jUnitContext.get(ApplicationContext.class);
        }

        @Override
        public Class<?> getTestClass() {
            return testContext.getTestClass();
        }

        @Override
        public Object getTestInstance() {
            return testContext.getTestInstance();
        }

        @Override
        public Method getTestMethod() {
            return testContext.getTestMethod();
        }

        @Override
        public Throwable getTestException() {
            return testContext.getTestException();
        }

        @Override
        public void markApplicationContextDirty(DirtiesContext.HierarchyMode hierarchyMode) {
            testContext.markApplicationContextDirty(hierarchyMode);
        }

        @Override
        public void updateState(Object o, Method method, Throwable throwable) {
            testContext.updateState(o, method, throwable);
        }

        @Override
        public void setAttribute(String s, Object o) {
            testContext.setAttribute(s, o);
        }

        @Override
        public Object getAttribute(String s) {
            return testContext.getAttribute(s);
        }

        @Override
        public Object removeAttribute(String s) {
            return testContext.removeAttribute(s);
        }

        @Override
        public boolean hasAttribute(String s) {
            return testContext.hasAttribute(s);
        }

        @Override
        public String[] attributeNames() {
            return testContext.attributeNames();
        }
    }
}

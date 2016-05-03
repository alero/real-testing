package org.hrodberaht.injection.extensions.junit;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class SpringJUnitRunner extends SpringJUnit4ClassRunner {


    final JUnitRunner jUnitRunner;

    /**
     * Creates a BlockJUnit4ClassRunner to run
     *
     * @param clazz
     * @throws InitializationError if the test class is malformed.
     */
    public SpringJUnitRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
        jUnitRunner = new JUnitRunner(clazz);
    }

    @Override
    protected void runChild(FrameworkMethod frameworkMethod, RunNotifier notifier) {
        try {
            jUnitRunner.beforeRunChild();
            super.runChild(frameworkMethod, notifier);
        } finally {
            jUnitRunner.afterRunChild();
        }
    }

    @Override
    protected Object createTest() throws Exception {
        Object testInstance = super.createTest();
        jUnitRunner.activeContainer.autowireAndPostConstruct(testInstance);
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
            TestContext testContext = (TestContext) field.get(contextManager);
            field.set(contextManager, new TestContextLocal(testContext));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    private class TestContextManagerLocal extends TestContextManager {

        public TestContextManagerLocal(Class<?> testClass) {
            super(testClass);
        }

        @Override
        public void registerTestExecutionListeners(List<TestExecutionListener> testExecutionListeners) {
            List<TestExecutionListener> listeners = testExecutionListeners
                    .stream().filter(t -> !(t instanceof DependencyInjectionTestExecutionListener))
                    .collect(Collectors.toList());
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
            return jUnitRunner.activeContainer.get(ApplicationContext.class);
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

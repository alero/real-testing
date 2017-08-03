package org.hrodberaht.injection.extensions.junit;

import org.hrodberaht.injection.extensions.spring.jpa.SpringEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class TestContextManagerLocal  {

    private static final Logger LOG = LoggerFactory.getLogger(TestContextManagerLocal.class);

    private final SpringJUnitRunner springJUnitRunner;
    private final JUnitRunner jUnitRunner;
    private final TestContextManager testContextManager;

    public TestContextManagerLocal(SpringJUnitRunner springJUnitRunner, JUnitRunner jUnitRunner, Class<?> testClass) {
        this.jUnitRunner = jUnitRunner;
        this.springJUnitRunner = springJUnitRunner;
        this.testContextManager = new TestContextManager(testClass){
            @Override
            public void registerTestExecutionListeners(List<TestExecutionListener> testExecutionListeners) {


                final List<TestExecutionListener> listeners = new ArrayList<>();
                testExecutionListeners
                        .stream()
                        .filter(t -> !(t instanceof DependencyInjectionTestExecutionListener))
                        .collect(Collectors.toList())
                        .forEach(t -> {
                                    if (t instanceof TransactionalTestExecutionListener) {
                                        springJUnitRunner.transactionalTestExecutionListener = new TransactionalTestExecutionListener() {
                                            @Override
                                            public void afterTestMethod(TestContext testContext) throws Exception {
                                                flushEntityManager();
                                                super.afterTestMethod(testContext);
                                            }
                                        };
                                        listeners.add(springJUnitRunner.transactionalTestExecutionListener);
                                    } else {
                                        listeners.add(t);
                                    }
                                }

                        );

                super.registerTestExecutionListeners(listeners);
            }
        };
    }

    private SpringEntityManager getSpringEntityManager() {
        try {
            return jUnitRunner.activeContainer.get(ApplicationContext.class)
                    .getBean(SpringEntityManager.class);
        } catch (Exception ex) {
            LOG.debug("SpringJUnitRunner info: " + ex.getMessage());
            return null;
        }
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

    public TestContextManager getContextManager() {
        return testContextManager;
    }
}

/*
 * Copyright (c) 2017 org.hrodberaht
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hrodberaht.injection.plugin.junit;

import org.hrodberaht.injection.core.InjectContainer;
import org.hrodberaht.injection.core.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.plugin.junit.inner.RunnerPlugins;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;

class JUnitContext {

    private static final Logger LOG = LoggerFactory.getLogger(JUnitContext.class);
    private InjectContainer activeContainer = null;
    private ContainerContextConfigBase containerConfig = null;
    private RunnerPlugins runnerPlugins = null;
    private final Class<?> clazz;

    /**
     * Creates a BlockJUnit4ClassRunner to run
     *
     * @throws InitializationError if the test class is malformed.
     */
    JUnitContext(Class<?> clazz) throws InitializationError {
        this.clazz = clazz;
        createContainerFromRegistration(clazz);
    }

    private void createContainerFromRegistration(Class<?> clazz) {
        try {
            Annotation[] annotations = clazz.getAnnotations();
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
        if (ContainerContextConfigBase.class.isAssignableFrom(testConfigClass)) {
            containerConfig = (ContainerContextConfigBase) testConfigClass.newInstance();
            runnerPlugins = getRunnerPlugins();
            runnerPlugins.runInitBeforeContainer();
            containerConfig.start();
            runnerPlugins.runInitAfterContainer(containerConfig.getActiveRegister());

            LOG.info("Creating creator for thread {}", Thread.currentThread().getName());
        } else {
            throw new IllegalAccessError("Currently the test config class must extrend ContainerContextConfigBase");
        }
    }

    private RunnerPlugins getRunnerPlugins() {
        if (containerConfig != null) {
            return containerConfig.getRunnerPlugins();
        } else {
            return new RunnerPlugins(new ConcurrentHashMap<>());
        }
    }

    interface OperationsRunner {
        void run();
    }

    interface OperationsErrorHandler {
        void handleError(Exception e);
    }

    interface TestCreator {
        Object create() throws Exception;
    }

    /**
     * @param frameworkMethod
     */
    void runChild(FrameworkMethod frameworkMethod, OperationsRunner operationsRunner, OperationsErrorHandler errorHandler) {
        try {
            runBeforeTest(true);
            try {
                // This will execute the createTest method below, the activeContainer handling relies on this.
                LOG.info("START running test " +
                        frameworkMethod.getName() + " for thread " + Thread.currentThread().getName());
                operationsRunner.run();
                LOG.info("END running test " +
                        frameworkMethod.getName() + " for thread " + Thread.currentThread().getName());
            } finally {
                runAfterTest();
            }
        } catch (Exception e) {
            LOG.error("Fatal test error :" + frameworkMethod.getName(), e);
            errorHandler.handleError(e);
        }
    }

    void runBeforeTest(boolean activateContainer) {
        runnerPlugins.runBeforeTest(containerConfig.getActiveRegister());

        containerConfig.beforeRunChild();

        if (activateContainer) {
            activateContainer();
        }
    }

    void activateContainer() {
        activeContainer = containerConfig.getActiveRegister().getContainer();
    }

    void runAfterTest() {
        runnerPlugins.runAfterTest(containerConfig.getActiveRegister());
        // TransactionManager.endTransaction();
        containerConfig.cleanActiveContainer();
    }


    public void run(OperationsRunner operationsRunner) {
        runBeforeClass();
        operationsRunner.run();
        runAfterClass();
    }

    void runAfterClass() {
        runnerPlugins.runAfterTestClass(containerConfig.getActiveRegister());
    }

    void runBeforeClass() {
        runnerPlugins.runBeforeTestClass(containerConfig.getActiveRegister());
    }

    /**
     * Runs the injection of dependencies and resources on the test case before returned
     *
     * @return the testcase
     * @throws Exception
     */
    Object createTest(TestCreator testCreator) throws Exception {
        Object testInstance = testCreator.create();
        autoWireTestObject(testInstance);
        return testInstance;
    }

    <T> T get(Class<T> aClass) {
        return activeContainer.get(aClass);
    }

    void autoWireTestObject(Object testInstance) {
        activeContainer.autowireAndPostConstruct(testInstance);
    }

}

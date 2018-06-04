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
import org.hrodberaht.injection.core.register.InjectionRegister;
import org.hrodberaht.injection.plugin.exception.JUnitRuntimeException;
import org.hrodberaht.injection.plugin.junit.api.PluginContext;
import org.hrodberaht.injection.plugin.junit.inner.RunnerPlugins;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

class JUnitContext {

    private static final Logger LOG = LoggerFactory.getLogger(JUnitContext.class);
    private final Class<?> testClazz;
    private final Class configClass;
    private InjectContainer activeContainer = null;
    private ContainerContextConfigBase containerConfig = null;
    private RunnerPlugins runnerPlugins = null;

    /**
     * Creates a BlockJUnit4ClassRunner to run
     *
     * @throws InitializationError if the test class is malformed.
     */
    JUnitContext(Class<?> clazz) {
        this.testClazz = clazz;
        this.configClass = getConfigClass();
        createUnitTestContext();
    }

    private Class getConfigClass() {

        Annotation[] annotations = testClazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == ContainerContext.class) {
                return ((ContainerContext) annotation).value();
            }
        }
        throw new JUnitRuntimeException("Could not find a ContainerContext annotation with config, this must exist when using the JUnitContext runner");

    }

    private void createUnitTestContext() {
        try {
            if (ContainerContextConfigBase.class.isAssignableFrom(configClass)) {
                containerConfig = (ContainerContextConfigBase) configClass.newInstance();
                runnerPlugins = getRunnerPlugins();
                containerConfig.initiateConfig(createContext());
                runnerPlugins.runInitBeforeContainer(createContext());
                containerConfig.buildConfig();
                runnerPlugins.runInitAfterContainer(createContext());

                LOG.info("Creating creator for thread {}", Thread.currentThread().getName());
            } else {
                throw new IllegalAccessError("Currently the test config class must extend ContainerContextConfigBase, I am working on a solution to this limitation");
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InjectRuntimeException(e);
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
     */
    void runChild(FrameworkMethod frameworkMethod, OperationsRunner operationsRunner, OperationsErrorHandler errorHandler) {
        try {
            runBeforeTest(true, frameworkMethod.getName());
            try {
                // This will execute the createTest method below, the activeContainer handling relies on this.
                LOG.info("Starting - test '{}' in class '{}' for thread '{}", frameworkMethod.getName(), testClazz.getSimpleName(), Thread.currentThread().getName());
                operationsRunner.run();
                LOG.debug("Ending - test '{}' in class '{}' for thread '{}", frameworkMethod.getName(), testClazz.getSimpleName(), Thread.currentThread().getName());
            } finally {
                runAfterTest(frameworkMethod.getName());
            }
        } catch (Exception e) {
            LOG.error("Fatal test error :" + frameworkMethod.getName(), e);
            errorHandler.handleError(e);
        }
    }

    void runBeforeTest(boolean activateContainer, String testName) {
        runnerPlugins.runBeforeTest(createContext(testName));

        containerConfig.beforeRunChild();

        if (activateContainer) {
            activateContainer();
        }
    }

    void runAfterTest(String testName) {
        runnerPlugins.runAfterTest(createContext(testName));
        containerConfig.cleanActiveContainer();
    }

    private PluginContext createContext() {
        return new RunnerPluginContext(null, testClazz, configClass, containerConfig.getActiveRegister());
    }

    private PluginContext createContext(String testName) {
        return new RunnerPluginContext(testName, testClazz, configClass, containerConfig.getActiveRegister());
    }

    void activateContainer() {
        activeContainer = containerConfig.getActiveRegister().getContainer();
    }

    public void run(OperationsRunner operationsRunner) {
        runBeforeClass();
        operationsRunner.run();
        runAfterClass();
    }

    void runAfterClass() {
        runnerPlugins.runAfterTestClass(createContext());
    }

    void runBeforeClass() {
        runnerPlugins.runBeforeTestClass(createContext());
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

    interface OperationsRunner {
        void run();
    }

    interface OperationsErrorHandler {
        void handleError(Exception e);
    }

    interface TestCreator {
        Object create() throws Exception;
    }

    private static class RunnerPluginContext implements PluginContext {
        private final Class<?> testClass;
        private final Class<?> configClass;
        private final InjectionRegister injectionRegister;
        private final String testName;


        private RunnerPluginContext(String testName, Class<?> testClass, Class<?> configClass, InjectionRegister injectionRegister) {
            this.testName = testName;
            this.testClass = testClass;
            this.configClass = configClass;
            this.injectionRegister = injectionRegister;
        }

        @Override
        public String getTestName() {
            return testName;
        }

        @Override
        public Class<?> getTestClass() {
            return testClass;
        }

        @Override
        public Class<?> getConfigClass() {
            return configClass;
        }

        @Override
        public InjectionRegister register() {
            return injectionRegister;
        }
    }

}

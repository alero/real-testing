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

package org.hrodberaht.injection.plugin.junit.plugins;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.inmemory.InMemoryTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainer;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.hrodberaht.injection.core.register.InjectionRegister;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.PluginContext;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterClassTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterContainerCreation;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeClassTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginContext;
import org.hrodberaht.injection.plugin.junit.jersey.JerseyTestBuilder;
import org.hrodberaht.injection.plugin.junit.jersey.JerseyTestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.LogManager;

public class JerseyPlugin implements Plugin {

    private static final Logger LOG = LoggerFactory.getLogger(JerseyPlugin.class);

    private static JerseyTestRunner suiteRunner;
    private static Map<Class, JerseyTestRunner> classRunnerMap = new ConcurrentHashMap<>();

    private JerseyTestRunner jerseyTestRunner;
    private ClientConfigInterface clientConfigInterface;
    private ResourceConfigInterface resourceConfigInterface;
    private TestContainerFactoryInterface testContainerFactoryInterface;
    private LifeCycle lifeCycle = LifeCycle.TEST;
    private Class testClass;

    @FunctionalInterface
    public interface ClientConfigInterface {
        void config(ClientConfig config);
    }

    @FunctionalInterface
    public interface ResourceConfigInterface {
        ResourceConfig config();
    }

    @FunctionalInterface
    public interface TestContainerFactoryInterface {
        TestContainerFactory container();
    }

    public JerseyPluginBuilder builder() {
        return new JerseyPluginBuilder(this);
    }


    public static class JerseyPluginBuilder {
        private final JerseyPlugin jerseyPlugin;

        protected JerseyPluginBuilder(JerseyPlugin jerseyPlugin) {
            this.jerseyPlugin = jerseyPlugin;
        }

        public JerseyPluginBuilder lifeCycle(LifeCycle lifeCycle) {
            jerseyPlugin.lifeCycle = lifeCycle;
            return this;
        }

        public JerseyPluginBuilder clientConfig(ClientConfigInterface clientConfigInterface) {
            jerseyPlugin.clientConfigInterface = clientConfigInterface;
            return this;
        }

        public JerseyPluginBuilder resourceConfig(ResourceConfigInterface resourceConfigInterface) {
            jerseyPlugin.resourceConfigInterface = resourceConfigInterface;
            return this;
        }

        public JerseyPluginBuilder resourceConfig(TestContainerFactoryInterface testContainerFactoryInterface) {
            jerseyPlugin.testContainerFactoryInterface = testContainerFactoryInterface;
            return this;
        }

    }

    private void initJerseyContainer() {
        if (lifeCycle == LifeCycle.TEST_SUITE && suiteRunner == null) {
            suiteRunner = createJerseyContainer();
            jerseyTestRunner = suiteRunner;
        } else if (lifeCycle == LifeCycle.TEST_CONFIG) {
            jerseyTestRunner = classRunnerMap.computeIfAbsent(testClass, aClass -> createJerseyContainer());
        } else {
            jerseyTestRunner = createJerseyContainer();
        }
    }

    private JerseyTestRunner createJerseyContainer() {

        LOG.info("Creating JerseyTestRunner for Thread {}", Thread.currentThread());

        readLoggingSettings();

        return new JerseyTestRunner(new JerseyTest() {
            @Override
            protected ResourceConfig configure() {
                return resourceConfigInterface == null ? new ResourceConfig() : resourceConfigInterface.config();
            }

            @Override
            protected void configureClient(ClientConfig config) {
                if (clientConfigInterface != null) {
                    clientConfigInterface.config(config);
                }
                super.configureClient(config);
            }

            @Override
            protected TestContainerFactory getTestContainerFactory() {
                if (testContainerFactoryInterface != null) {
                    return testContainerFactoryInterface.container();
                }
                return new InMemoryTestContainerFactory() {
                    @Override
                    public TestContainer create(URI baseUri, DeploymentContext context) {
                        return new TestContainerWrapper(super.create(baseUri, context));
                    }
                };
            }
        });
    }

    public JerseyTestBuilder testBuilder() {
        return new JerseyTestBuilder("http://localhost:9998", jerseyTestRunner.getJerseyTest());
    }

    private void readLoggingSettings() {
        try {
            InputStream inputStream = JerseyPlugin.class.getResourceAsStream("/logging.properties");
            if (inputStream != null) {
                LogManager.getLogManager().readConfiguration(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @RunnerPluginAfterContainerCreation
    private void beforeContainerCreation() {
        initJerseyContainer();
        if (lifeCycle == LifeCycle.TEST_SUITE) {
            startJersey();
        }
    }

    @RunnerPluginBeforeClassTest
    private void beforeTestClass(InjectionRegister injectionRegister) {
        if (lifeCycle == LifeCycle.TEST_CLASS || lifeCycle == LifeCycle.TEST_CONFIG) {
            startJersey();
        }
    }

    @RunnerPluginAfterClassTest
    private void afterTestClass(InjectionRegister injectionRegister) {
        if (lifeCycle == LifeCycle.TEST_CLASS) {
            stopJersey();
        }
    }

    @RunnerPluginBeforeTest
    private void beforeTest(InjectionRegister injectionRegister) {
        if (lifeCycle == LifeCycle.TEST) {
            startJersey();
        }
    }

    @RunnerPluginAfterTest
    private void afterTest(InjectionRegister injectionRegister) {
        if (lifeCycle == LifeCycle.TEST) {
            stopJersey();
        }
    }

    @RunnerPluginContext
    private void testContext(PluginContext pluginContext) {
        testClass = pluginContext.getTestClass();
    }

    private void startJersey() {
        LOG.info("Starting JerseyTest Container for Thread {}", Thread.currentThread());
        jerseyTestRunner.initializeJersey();
    }

    private void stopJersey() {
        LOG.info("Stopping JerseyTest Container for Thread {}", Thread.currentThread());
        jerseyTestRunner.shutdownJersey();
    }






    @Override
    public LifeCycle getLifeCycle() {
        return lifeCycle;
    }


}

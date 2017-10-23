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
import org.hrodberaht.injection.plugin.junit.jersey.JerseyTestBuilder;
import org.hrodberaht.injection.plugin.junit.jersey.JerseyTestRunner;
import org.hrodberaht.injection.plugin.junit.spi.RunnerPlugin;
import org.hrodberaht.injection.register.InjectionRegister;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.logging.LogManager;

public class JerseyPlugin implements RunnerPlugin {

    private JerseyTestRunner jerseyTestRunner;
    private ClientConfigInterface clientConfigInterface;
    private ResourceConfigInterface resourceConfigInterface;
    private TestContainerFactoryInterface testContainerFactoryInterface;

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

    public JerseyPluginBuilder build() {
        return new JerseyPluginBuilder(this);
    }


    public static class JerseyPluginBuilder {
        private final JerseyPlugin jerseyPlugin;

        private JerseyPluginBuilder(JerseyPlugin jerseyPlugin) {
            this.jerseyPlugin = jerseyPlugin;
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

        readLoggingSettings();

        jerseyTestRunner = new JerseyTestRunner(new JerseyTest() {
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


    @Override
    public void beforeContainerCreation() {


    }

    @Override
    public void afterContainerCreation(InjectionRegister injectionRegister) {
        initJerseyContainer();
    }

    @Override
    public void beforeTest(InjectionRegister injectionRegister) {
        jerseyTestRunner.initializeJersey();
        /*
        injectionRegister.register(new RegistrationModuleAnnotation() {
            @Override
            public void registrations() {
                JerseyTestBuilder jerseyTestBuilder = new JerseyTestBuilder("http://localhost:9998", jerseyTest);
                register(JerseyTestBuilder.class).withFactoryInstance(jerseyTestBuilder);
            }
        });
        */
    }

    @Override
    public void beforeTestClass(InjectionRegister injectionRegister) {

    }

    @Override
    public void afterTestClass(InjectionRegister injectionRegister) {

    }

    @Override
    public void afterTest(InjectionRegister injectionRegister) {
        jerseyTestRunner.shutdownJersey();
    }

    @Override
    public LifeCycle getLifeCycle() {
        return LifeCycle.TEST_SUITE;
    }


}

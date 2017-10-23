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

import org.hrodberaht.injection.config.ContainerConfig;
import org.hrodberaht.injection.internal.ResourceInject;
import org.hrodberaht.injection.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.plugin.junit.inner.AnnotatedInjectionPlugin;
import org.hrodberaht.injection.plugin.junit.inner.AnnotatedResourcePlugin;
import org.hrodberaht.injection.plugin.junit.inner.AnnotatedRunnerPlugin;
import org.hrodberaht.injection.plugin.junit.inner.RunnerPlugins;
import org.hrodberaht.injection.plugin.junit.resources.ChainableInjectionPointProvider;
import org.hrodberaht.injection.plugin.junit.resources.PluggableResourceFactory;
import org.hrodberaht.injection.plugin.junit.resources.ResourcePluginBase;
import org.hrodberaht.injection.plugin.junit.spi.InjectionPlugin;
import org.hrodberaht.injection.plugin.junit.spi.Plugin;
import org.hrodberaht.injection.plugin.junit.spi.RunnerPlugin;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.spi.ResourceFactory;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ContainerContextConfigBase implements ContainerContextConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ContainerContextConfigBase.class);
    private final Map<Class<? extends Plugin>, Plugin> activePlugins = new ConcurrentHashMap<>();
    private final RunnerPlugins runnerPlugins = new RunnerPlugins(activePlugins);
    private final ContainerConfigInner containerConfigInner = new ContainerConfigInner(this);

    public abstract void register(InjectionRegistryBuilder registryBuilder);

    protected <T extends Plugin> T activatePlugin(Class<T> pluginClass) {
        return containerConfigInner.activatePlugin(pluginClass);
    }

    RunnerPlugins getRunnerPlugins() {
        return runnerPlugins;
    }

    void cleanActiveContainer() {
        containerConfigInner.cleanActiveContainer();
    }

    InjectionRegister getActiveRegister() {
        return containerConfigInner.getActiveRegister();
    }


    void beforeRunChild() {
        containerConfigInner.addSingletonActiveRegistry();
    }


    void start() {
        containerConfigInner.start();
    }

    private static class ContainerConfigInner extends ContainerConfig {
        private final ContainerContextConfigBase base;
        private InjectionPlugin injectionPlugin;
        private ChainableInjectionPointProvider chainableInjectionPointProvider;

        private ContainerConfigInner(ContainerContextConfigBase base) {
            this.base = base;
        }

        @Override
        protected ResourceFactory createResourceFactory() {
            return new PluggableResourceFactory();
        }

        @Override
        protected ResourceInject createResourceInject() {
            return new ResourceInject();
        }

        @Override
        protected InjectionFinder createDefaultInjectionPointFinder() {
            if (chainableInjectionPointProvider != null) {
                return chainableInjectionPointProvider;
            }
            return lookupInjectionPointFinder();
        }

        private InjectionFinder lookupInjectionPointFinder() {
            if (injectionPlugin != null) {
                return injectionPlugin.getInjectionFinder(this);
            }
            return super.createDefaultInjectionPointFinder();
        }

        private <T extends Plugin> T activatePlugin(Class<T> pluginClass) {
            final T plugin = createPlugin(pluginClass);
            base.registerActivePlugin(pluginClass, plugin);
            injectionRegistryBuilder.register(registrations -> registrations.register(new RegistrationModuleAnnotation() {
                @Override
                public void registrations() {
                    register(pluginClass).withFactoryInstance(plugin);
                }
            }));
            if (plugin instanceof ResourcePluginBase) {
                LOG.info("adding PluggableResourceFactory for {}", plugin.getClass().getSimpleName());
                PluggableResourceFactory.setPluggableResourceFactory(
                        (ResourcePluginBase) plugin, resourceFactory
                );
            }
            if (AnnotatedResourcePlugin.containsAnnotations(plugin)) {
                LOG.info("Activating annotated ResourcePlugin {}", plugin.getClass().getSimpleName());
                AnnotatedResourcePlugin.inject(resourceFactory, plugin);
                if (AnnotatedResourcePlugin.hasChainableAnnotaion(plugin)) {
                    if (chainableInjectionPointProvider == null) {
                        chainableInjectionPointProvider = AnnotatedResourcePlugin.getChainableInjectionPointProvider(plugin, lookupInjectionPointFinder());
                    } else {
                        chainableInjectionPointProvider = AnnotatedResourcePlugin.getChainableInjectionPointProvider(plugin, chainableInjectionPointProvider);
                    }
                }
            }
            if (AnnotatedInjectionPlugin.containsAnnotations(plugin)) {
                LOG.info("Activating annotated InjectionPlugin {}", plugin.getClass().getSimpleName());
                if (this.injectionPlugin == null) {
                    if (chainableInjectionPointProvider != null) {
                        throw new RuntimeException("A plugin that has created a Injection provider already exxists, change the order of the plugins to get the InjectionPlugin to go active first");
                    }
                    this.injectionPlugin = AnnotatedInjectionPlugin.createPluginWrapper(plugin);
                } else {
                    throw new RuntimeException("There can be only one InjectionPlugin active at once");
                }
            }
            if (plugin instanceof InjectionPlugin) {
                LOG.info("Activating InjectionPlugin {}", plugin.getClass().getSimpleName());
                InjectionPlugin injectionPlugin = (InjectionPlugin) plugin;
                if (this.injectionPlugin == null) {
                    this.injectionPlugin = injectionPlugin;
                } else {
                    throw new RuntimeException("There can be only one InjectionPlugin active at once");
                }
            }
            return plugin;
        }

        private <T extends Plugin> T createPlugin(Class<T> pluginClass) {
            try {
                T plugin = pluginClass.newInstance();
                if (plugin instanceof RunnerPlugin) {
                    LOG.info("Activating RunnerPlugin {}", plugin.getClass().getSimpleName());
                    return base.runnerPlugins.addPlugin((RunnerPlugin) plugin);
                }
                if (containsRunnerAnnotation(plugin)) {
                    LOG.info("Activating Runner annotated Plugin {}", plugin.getClass().getSimpleName());
                    return base.runnerPlugins.addAnnotatedPlugin(plugin);
                }
                LOG.info("Plugin does not container runner info {}", plugin.getClass().getSimpleName());
                return plugin;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private <T extends Plugin> boolean containsRunnerAnnotation(T plugin) {
            return AnnotatedRunnerPlugin.containsRunnerAnnotations(plugin);
        }

        @Override
        public void injectResources(Object serviceInstance) {
            PluggableResourceFactory pluggableResourceFactory = (PluggableResourceFactory) resourceFactory;
            resourceInjection.injectResources(pluggableResourceFactory.getTypedMap(), pluggableResourceFactory.getNamedMap(), serviceInstance);
        }

        @Override
        protected void appendResources(InjectionRegister registerModule) {
            PluggableResourceFactory pluggableResourceFactory = (PluggableResourceFactory) resourceFactory;

            pluggableResourceFactory.getNamedMap().forEach((resourceKey, value) -> {
                registerModule.register(new RegistrationModuleAnnotation() {
                    @Override
                    public void registrations() {
                        register(resourceKey.getType()).named(resourceKey.getName()).withInstance(value);
                    }
                });
            });

            pluggableResourceFactory.getTypedMap().forEach((aClass, value) -> {
                registerModule.register(new RegistrationModuleAnnotation() {
                    @Override
                    public void registrations() {
                        register(aClass).withInstance(value);
                    }
                });
            });


        }

        @Override
        public void register(InjectionRegistryBuilder registryBuilder) {
            base.register(registryBuilder);
            if (injectionPlugin != null) {
                // Once the user has registered all resources needed, we bind it to the selected injection plugin
                injectionPlugin.setInjectionRegister(registryBuilder.getInjectionRegister());
            }
        }
    }

    private <T extends Plugin> void registerActivePlugin(Class<T> pluginClass, T plugin) {
        activePlugins.put(pluginClass, plugin);
    }


}

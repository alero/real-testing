package org.hrodberaht.injection.plugin.junit;

import org.hrodberaht.injection.config.ContainerConfig;
import org.hrodberaht.injection.internal.ResourceInject;
import org.hrodberaht.injection.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.plugin.junit.inner.AnnotatedRunnerPlugin;
import org.hrodberaht.injection.plugin.junit.resources.ChainableInjectionPointProvider;
import org.hrodberaht.injection.plugin.junit.resources.PluggableResourceFactory;
import org.hrodberaht.injection.plugin.junit.spi.InjectionPlugin;
import org.hrodberaht.injection.plugin.junit.spi.Plugin;
import org.hrodberaht.injection.plugin.junit.spi.PluginConfig;
import org.hrodberaht.injection.plugin.junit.spi.ResourcePlugin;
import org.hrodberaht.injection.plugin.junit.spi.RunnerPlugin;
import org.hrodberaht.injection.plugin.junit.inner.RunnerPlugins;
import org.hrodberaht.injection.plugin.junit.spi.annotation.RunnerPluginAfterContainerCreation;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.spi.JavaResourceCreator;
import org.hrodberaht.injection.spi.ResourceFactory;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public abstract class PluggableContainerConfigBase implements PluginConfig {

    private static final Logger LOG = LoggerFactory.getLogger(PluggableContainerConfigBase.class);
    private final Map<Class<? extends Plugin>, Plugin> activePlugins = new ConcurrentHashMap<>();
    private final RunnerPlugins runnerPlugins = new RunnerPlugins(activePlugins);
    private final ContainerConfigInner containerConfigInner = new ContainerConfigInner(this);

    protected abstract void register(InjectionRegistryBuilder registryBuilder);

    protected <T extends Plugin> T activatePlugin(Class<T> pluginClass) {
        return containerConfigInner.activatePlugin(pluginClass);
    }

    protected <T extends Plugin> T getPlugin(Class<T> pluginClass) {
        return (T) activePlugins.get(pluginClass);
    }

    protected <T> JavaResourceCreator<T> getCreator(Class<T> type) {
        return containerConfigInner.getResourceFactory().getCreator(type);
    }

    @Override
    public RunnerPlugins getRunnerPlugins() {
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
        private final PluggableContainerConfigBase base;
        private InjectionPlugin injectionPlugin;
        private ResourcePlugin resourcePlugin;

        private ContainerConfigInner(PluggableContainerConfigBase base) {
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
            if (injectionPlugin != null) {
                return wrap(injectionPlugin.getInjectionFinder(this));
            }
            return wrap(super.createDefaultInjectionPointFinder());
        }

        private InjectionFinder wrap(InjectionFinder injectionFinder) {
            if(resourcePlugin != null) {
                ChainableInjectionPointProvider chainableInjectionPointProvider = resourcePlugin.getInjectionProvider(injectionFinder);
                if (chainableInjectionPointProvider != null) {
                    return chainableInjectionPointProvider;
                }
            }
            return injectionFinder;
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
            if (plugin instanceof ResourcePlugin) {
                LOG.info("Activating ResourcePlugin {}", plugin.getClass().getSimpleName());
                resourcePlugin = (ResourcePlugin) plugin;
                PluggableResourceFactory pluggableResourceFactory = (PluggableResourceFactory) resourceFactory;
                pluggableResourceFactory.addCustomCreator(resourcePlugin);
                resourcePlugin.setPluggableResourceFactory(pluggableResourceFactory);
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
                if(containsRunnerAnnotation(plugin)){
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

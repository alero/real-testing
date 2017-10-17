package org.hrodberaht.injection.extensions.plugin.junit;

import org.hrodberaht.injection.config.ContainerConfig;
import org.hrodberaht.injection.extensions.plugin.junit.resources.PluggableResourceFactory;
import org.hrodberaht.injection.extensions.plugin.junit.spi.*;
import org.hrodberaht.injection.internal.ResourceInject;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.spi.JavaResourceCreator;
import org.hrodberaht.injection.spi.ResourceFactory;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PluggableContainerConfigBase implements PluginConfig{

    private final RunnerPlugins runnerPlugins = new RunnerPlugins();
    private final ContainerConfigInner containerConfigInner = new ContainerConfigInner(this);
    private final Map<Class<? extends Plugin>, Plugin> activePlugins = new ConcurrentHashMap<>();
    protected abstract void register(InjectionRegistryBuilder registryBuilder);

    protected <T extends Plugin> T activatePlugin(Class<T> pluginClass) {
        return containerConfigInner.activatePlugin(pluginClass);
    }

    protected  <T extends Plugin> T getPlugin(Class<T> pluginClass ) {
        return (T) activePlugins.get(pluginClass);
    }

    protected <T> JavaResourceCreator<T> getCreator(Class<T> type){
        return containerConfigInner.getResourceFactory().getCreator(type);
    }

    @Override
    public RunnerPlugins getRunnerPlugins(){
        return runnerPlugins;
    }

    void cleanActiveContainer() {
        containerConfigInner.cleanActiveContainer();
    }


    InjectionRegister getActiveRegister() {
        return containerConfigInner.getActiveRegister();
    }


    void addSingletonActiveRegistry() {
        containerConfigInner.addSingletonActiveRegistry();
    }


    void start() {
        containerConfigInner.start();
    }

    private static class ContainerConfigInner extends ContainerConfig{
        private final PluggableContainerConfigBase base;

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

        private <T extends Plugin> T activatePlugin(Class<T> pluginClass) {
            T plugin = createPlugin(pluginClass);
            base.activePlugins.put(pluginClass, plugin);
            if(plugin instanceof ResourcePlugin){
                PluggableResourceFactory pluggableResourceFactory = (PluggableResourceFactory)resourceFactory;
                pluggableResourceFactory.addCustomCreator((ResourcePlugin)plugin);
            }
            if(plugin instanceof RunnerPlugin){
                base.runnerPlugins.addPlugin((RunnerPlugin) plugin);
            }
            return plugin;
        }

        private <T extends Plugin> T createPlugin(Class<T> pluginClass) {
            try {
                return pluginClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void injectResources(Object serviceInstance) {
            PluggableResourceFactory pluggableResourceFactory = (PluggableResourceFactory)resourceFactory;
            resourceInjection.injectResources(pluggableResourceFactory.getTypedMap(), pluggableResourceFactory.getNamedMap(), serviceInstance);
        }

        @Override
        protected void appendResources(InjectionRegister registerModule) {
            PluggableResourceFactory pluggableResourceFactory = (PluggableResourceFactory)resourceFactory;

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
        }
    }











}

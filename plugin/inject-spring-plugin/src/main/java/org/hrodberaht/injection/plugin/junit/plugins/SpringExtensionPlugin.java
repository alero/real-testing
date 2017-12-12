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

import org.hrodberaht.injection.core.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.injection.core.register.InjectionRegister;
import org.hrodberaht.injection.core.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.core.spi.ContainerConfigBuilder;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.PluginContext;
import org.hrodberaht.injection.plugin.junit.api.annotation.InjectionPluginInjectionFinder;
import org.hrodberaht.injection.plugin.junit.api.annotation.InjectionPluginInjectionRegister;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeContainerCreation;
import org.hrodberaht.injection.plugin.junit.api.resource.ResourceProvider;
import org.hrodberaht.injection.plugin.junit.api.resource.ResourceProviderSupport;
import org.hrodberaht.injection.plugin.junit.plugins.common.PluginLifeCycledResource;
import org.hrodberaht.injection.plugin.junit.spring.beans.config.ContainerAllSpringConfig;
import org.hrodberaht.injection.plugin.junit.spring.beans.config.ContainerSpringConfig;
import org.hrodberaht.injection.plugin.junit.spring.injector.SpringBeanInjector;
import org.hrodberaht.injection.plugin.junit.spring.injector.SpringInject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * The SpringExtensionPlugin will start a Spring Container and use it internally as bridge.
 */
public class SpringExtensionPlugin implements Plugin {

    private static final Logger LOG = LoggerFactory.getLogger(SpringExtensionPlugin.class);

    private PluginLifeCycledResource<SpringRunner> pluginLifeCycledResource = new PluginLifeCycledResource<>(SpringRunner.class);

    private LifeCycle lifeCycle = LifeCycle.TEST_CONFIG;
    private Builder builder = new Builder();
    private InjectionRegister injectionRegister;
    private SpringRunner springRunner;

    @Override
    public LifeCycle getLifeCycle() {
        LOG.info("using lifeCycle:{} for plugin {}", lifeCycle, this);
        return lifeCycle;
    }

    public SpringExtensionPlugin springConfig(String... config){
        builder.springConfigFiles = config;
        return this;
    }

    public SpringExtensionPlugin springConfig(Class... config){
        builder.springConfigsClasses = config;
        return this;
    }

    public SpringExtensionPlugin lifeCycle(LifeCycle lifeCycle){
        this.lifeCycle = lifeCycle;
        return this;
    }

    public SpringExtensionPlugin with(Plugin plugin){
        if(plugin instanceof ResourceProviderSupport) {
            for(ResourceProvider resourceProvider:((ResourceProviderSupport)plugin).resources()){
                LOG.info("Adding resource {} - {}", resourceProvider.getName(), resourceProvider.getType());
                builder.resourceProviders.add(resourceProvider);
            }
        }else{
            LOG.warn("Plugin {} does not implement ResourceProviderSupport to exponse resources", plugin.getClass().getName());
        }
        builder.hasJpaPlugin = hasJpaPLugin(plugin);
        return this;
    }

    public SpringExtensionPlugin withResources(ResourceProviderSupport resourceProviderSupport){
        builder.resourceProviders.addAll(resourceProviderSupport.resources());
        return this;
    }

    private boolean hasJpaPLugin(Plugin plugin) {
        try {
            Class.forName("org.hrodberaht.injection.plugin.junit.plugins.JpaPlugin");
            return plugin instanceof JpaPlugin;
        }catch (ClassNotFoundException e){
            return false;
        }
    }

    private static class SpringRunner {
        // Basic Injection
        private final SpringBeanInjector springBeanInjector;
        private final SpringExtensionPlugin springExtensionPlugin;
        // Spring
        private final ApplicationContext context;
        private final DefaultListableBeanFactory parentBeanFactory = new DefaultListableBeanFactory();

        private SpringRunner(SpringExtensionPlugin springExtensionPlugin) {
            LOG.info("SpringRunner - Creating SpringApplication", this.getClass());
            Builder builder = springExtensionPlugin.builder;
            this.springExtensionPlugin = springExtensionPlugin;

            resourcesAsSpringBeans();

            if(builder.springConfigsClasses != null){
                context = loadConfig(builder.springConfigsClasses);
            }else if(builder.springConfigFiles != null){
                context = loadConfig(builder.springConfigFiles);
            }else {
                context = null;
            }
            if(context != null) {
                springBeanInjector = new SpringBeanInjector(context);
            }else{
                springBeanInjector = null;
            }
        }

        private void resourcesAsSpringBeans() {
            LOG.info("resourcesAsSpringBeans for {}", this);
            springExtensionPlugin.builder.resourceProviders.forEach(pluginResource -> {
                Object instance = getInstance(pluginResource);
                if(pluginResource.getName() != null) {
                    LOG.info("spring registerSingleton for {} using instance {}", pluginResource.getName(), instance);
                    parentBeanFactory.registerSingleton(pluginResource.getName(), instance);
                }else if(pluginResource.getType() != null){
                    LOG.info("spring registerResolvableDependency for {} using instance {}", pluginResource.getType(), instance);
                    parentBeanFactory.registerResolvableDependency(pluginResource.getType(), instance);
                }else{
                    LOG.warn("No name or type was provided for {}", pluginResource);
                }
            });
        }

        private Object getInstance(ResourceProvider resourceProvider) {
            return resourceProvider.getInstance();
        }

        private Class<?> getContainerSpringConfigClass() {
            return  springExtensionPlugin.builder.hasJpaPlugin ? ContainerAllSpringConfig.class : ContainerSpringConfig.class;
        }

        private ClassPathXmlApplicationContext loadConfig(String... springConfigs) {
            LOG.info("SpringContainerConfigBase - Creating SpringApplication XML for {}", this.getClass());
            validateEmptyContext(context);
            // TODO: we have no "jpa enabled" spring bean XML file ...
            String testSpringConfig = "/META-INF/container-spring-config.xml";
            String[] config = new String[]{testSpringConfig};
            if (springConfigs != null) {
                Stream<String> stringStream = Stream.concat(Stream.of(springConfigs), Stream.of(testSpringConfig));
                config = stringStream.toArray(String[]::new);
            }
            return new ClassPathXmlApplicationContext(config);
        }

        private AnnotationConfigApplicationContext loadConfig(Class... springConfigs) {
            LOG.info("SpringContainerConfigBase - Creating SpringApplication Java for {}", this.getClass());
            validateEmptyContext(context);
            Class[] config = new Class[]{getContainerSpringConfigClass()};
            if (springConfigs != null) {
                Stream<Class> stringStream = Stream.concat(Stream.of(springConfigs), Stream.of(getContainerSpringConfigClass()));
                config = stringStream.toArray(Class[]::new);
            }
            AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(parentBeanFactory);
            applicationContext.register(config);
            applicationContext.refresh();
            return applicationContext;
        }

        private void validateEmptyContext(ApplicationContext context) {
            if (context != null) {
                throw new IllegalStateException("Context is already loaded, can only be loaded once");
            }
        }

    }


    private static class Builder {
        private String[] springConfigFiles = null;
        private Class[] springConfigsClasses = null;
        private List<ResourceProvider> resourceProviders = new ArrayList<>();
        private boolean hasJpaPlugin= false;

    }


    private static class SpringInjectionPointFinder extends DefaultInjectionPointFinder {
        private final SpringExtensionPlugin springExtensionPlugin;

        private SpringInjectionPointFinder(SpringExtensionPlugin springExtensionPlugin, ContainerConfigBuilder containerConfigBuilder) {
            super(containerConfigBuilder);
            this.springExtensionPlugin = springExtensionPlugin;
        }

        @Override
        public Object extendedInjection(Object instance) {
            super.extendedInjection(instance);
            if(springExtensionPlugin.springRunner.springBeanInjector != null) {
                springExtensionPlugin.springRunner.springBeanInjector.inject(instance, springExtensionPlugin.injectionRegister.getContainer());
            }
            return instance;
        }

        @Override
        protected boolean hasInjectAnnotationOnField(Field field) {
            return field.isAnnotationPresent(SpringInject.class) || super.hasInjectAnnotationOnField(field);
        }

        @Override
        protected boolean hasInjectAnnotationOnMethod(Method method) {
            return method.isAnnotationPresent(SpringInject.class) || super.hasInjectAnnotationOnMethod(method);
        }
    }

    @RunnerPluginBeforeContainerCreation
    protected void beforeContainerCreation(PluginContext pluginContext) {
        LOG.info("beforeContainerCreation for {}", this);
        springRunner = pluginLifeCycledResource.create(lifeCycle, pluginContext, () -> new SpringRunner(this));
        injectionRegister.register(new RegistrationModuleAnnotation() {
            @Override
            public void registrations() {
                register(ApplicationContext.class).withFactoryInstance(springRunner.context);
            }
        });
    }

    @InjectionPluginInjectionRegister
    protected void setInjectionRegister(InjectionRegister injectionRegister) {
        this.injectionRegister = injectionRegister;
    }

    @InjectionPluginInjectionFinder
    protected DefaultInjectionPointFinder getInjectionFinder(ContainerConfigBuilder containerConfigBuilder) {
        return new SpringInjectionPointFinder(this, containerConfigBuilder);
    }
}

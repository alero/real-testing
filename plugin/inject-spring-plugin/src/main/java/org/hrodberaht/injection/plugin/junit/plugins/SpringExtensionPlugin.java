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
import org.hrodberaht.injection.core.spi.ContainerConfigBuilder;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.annotation.InjectionPluginInjectionFinder;
import org.hrodberaht.injection.plugin.junit.api.annotation.InjectionPluginInjectionRegister;
import org.hrodberaht.injection.plugin.junit.spring.beans.config.ContainerAllSpringConfig;
import org.hrodberaht.injection.plugin.junit.spring.beans.config.ContainerSpringConfig;
import org.hrodberaht.injection.plugin.junit.spring.injector.SpringBeanInjector;
import org.hrodberaht.injection.plugin.junit.spring.injector.SpringInject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * The SpringExtensionPlugin will start a Spring Container and use it internally as bridge.
 * It currently can not automatically wire @Inject fields over to spring, so use @Autowired in the testclasses to get the bridge working.
 * This limitation will be fixed in a future release, coming soon.
 */
public class SpringExtensionPlugin implements Plugin {

    private static final Logger LOG = LoggerFactory.getLogger(SpringExtensionPlugin.class);
    private static final Map<Class, SpringExtensionPlugin> CACHE = new ConcurrentHashMap<>();

    private ApplicationContext context;
    private SpringBeanInjector springBeanInjector;
    private boolean enableJPA = false;
    private final boolean enabledCache = System.getProperty("hrodberaht.test.spring.cache") != null;


    private InjectionRegister injectionRegister;

    @Override
    public LifeCycle getLifeCycle() {
        return LifeCycle.TEST_SUITE;
    }

    public SpringExtensionPlugin loadConfig(String... springConfigs) {
        validateEmptyContext(context);
        String testSpringConfig = "/META-INF/container-spring-config.xml";
        String[] config = new String[]{testSpringConfig};
        if (springConfigs != null) {
            Stream<String> stringStream = Stream.concat(Stream.of(springConfigs), Stream.of(testSpringConfig));
            config = stringStream.toArray(String[]::new);
        }
        context = new ClassPathXmlApplicationContext(config);
        springBeanInjector = new SpringBeanInjector(context);
        return this;
    }


    public SpringExtensionPlugin loadConfig(Class... springConfigs) {
        SpringExtensionPlugin configBase = CACHE.get(this.getClass());
        if (configBase != null && enabledCache) {
            LOG.debug("SpringContainerConfigBase - Using cached SpringApplication for " + this.getClass());
            context = configBase.context;
            springBeanInjector = configBase.springBeanInjector;
        } else {
            LOG.debug("SpringContainerConfigBase - Creating SpringApplication for " + this.getClass());
            validateEmptyContext(context);
            Class[] config = new Class[]{getContainerSpringConfigClass()};
            if (springConfigs != null) {
                Stream<Class> stringStream = Stream.concat(Stream.of(springConfigs), Stream.of(getContainerSpringConfigClass()));
                config = stringStream.toArray(Class[]::new);
            }
            context = new AnnotationConfigApplicationContext(config);
            springBeanInjector = new SpringBeanInjector(context);
            CACHE.put(this.getClass(), this);
        }
        return this;
    }

    /**
     * Makes it possible to use SpringEntityManager as autowirable resource in tests
     *
     * @return builder pattern
     * @see org.hrodberaht.injection.plugin.junit.spring.beans.SpringEntityManager
     */
    public SpringExtensionPlugin enableJPA() {
        enableJPA = true;
        return this;
    }


    private Class<?> getContainerSpringConfigClass() {
        return enableJPA ? ContainerAllSpringConfig.class : ContainerSpringConfig.class;
    }

    private void validateEmptyContext(ApplicationContext context) {
        if (context != null) {
            throw new IllegalStateException("Context is already loaded, can only be loaded once");
        }
    }

    private static class SpringInjectionPointFinder extends DefaultInjectionPointFinder {
        private final SpringExtensionPlugin springExtensionPlugin;

        private SpringInjectionPointFinder(SpringExtensionPlugin springExtensionPlugin, ContainerConfigBuilder containerConfigBuilder) {
            super(containerConfigBuilder);
            this.springExtensionPlugin = springExtensionPlugin;
        }

        @Override
        public Object extendedInjection(Object service) {
            super.extendedInjection(service);
            springExtensionPlugin.springBeanInjector.inject(service, springExtensionPlugin.injectionRegister.getContainer());
            return service;
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

    @InjectionPluginInjectionRegister
    protected void setInjectionRegister(InjectionRegister injectionRegister) {
        this.injectionRegister = injectionRegister;
    }

    @InjectionPluginInjectionFinder
    protected DefaultInjectionPointFinder getInjectionFinder(ContainerConfigBuilder containerConfigBuilder) {
        return new SpringInjectionPointFinder(this, containerConfigBuilder);
    }
}

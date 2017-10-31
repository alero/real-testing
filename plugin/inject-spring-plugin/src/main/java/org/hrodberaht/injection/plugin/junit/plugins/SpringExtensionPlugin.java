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
import org.hrodberaht.injection.plugin.exception.PluginRuntimeException;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.PluginContext;
import org.hrodberaht.injection.plugin.junit.api.annotation.InjectionPluginInjectionFinder;
import org.hrodberaht.injection.plugin.junit.api.annotation.InjectionPluginInjectionRegister;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterContainerCreation;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeContainerCreation;
import org.hrodberaht.injection.plugin.junit.datasource.TransactionManager;
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
import java.util.stream.Stream;

/**
 * The SpringExtensionPlugin will start a Spring Container and use it internally as bridge.
 * It currently can not automatically wire @Inject fields over to spring, so use @Autowired in the testclasses to get the bridge working.
 * This limitation will be fixed in a future release, coming soon.
 */
public class SpringExtensionPlugin implements Plugin {

    private static final Logger LOG = LoggerFactory.getLogger(SpringExtensionPlugin.class);

    private PluginLifeCycledResource<SpringRunner> pluginLifeCycledResource = new PluginLifeCycledResource<>(SpringRunner.class);

    private ResourceLifeCycle lifeCycle = ResourceLifeCycle.TEST_CONFIG;
    private Builder builder = new Builder(this);
    private InjectionRegister injectionRegister;
    private SpringRunner springRunner;

    @Override
    public LifeCycle getLifeCycle() {
        LOG.info("using lifeCycle:{} for plugin {}", lifeCycle, this);
        return  LifeCycle.TEST_CONFIG;
    }

    public SpringExtensionPlugin springConfig(String... config){
        builder.springConfigFiles = config;
        return this;
    }

    public SpringExtensionPlugin springConfig(Class... config){
        builder.springConfigsClasses = config;
        return this;
    }

    public SpringExtensionPlugin lifeCycle(ResourceLifeCycle lifeCycle){
        this.lifeCycle = lifeCycle;
        return this;
    }

    public SpringExtensionPlugin withDataSource(DataSourcePlugin dataSourcePlugin) {
        builder.dataSourcePluginWrapper = new DataSourcePluginWrapper(dataSourcePlugin, builder);
        return this;
    }

    public DataSourcePluginWrapper datasource() {
        if (builder.dataSourcePluginWrapper == null) {
            throw new PluginRuntimeException("datasource is not yet added as dependency plugin");
        }
        return builder.dataSourcePluginWrapper;
    }

    public SpringExtensionPlugin withSolr(Plugin plugin) {
        try {
            Class.forName(SolrJPlugin.class.getName());
            SolrJPlugin solrJPlugin = (SolrJPlugin)plugin;
            builder.solrJPluginWrapper = new SolrJPluginWrapper(solrJPlugin, builder);
        }catch (ClassNotFoundException e){
            throw new PluginRuntimeException(e);
        }
        return this;
    }

    public SolrJPluginWrapper solr() {
        if (builder.solrJPluginWrapper == null) {
            throw new PluginRuntimeException("solr is not yet added as dependency plugin");
        }
        return builder.solrJPluginWrapper;
    }

    private static class SpringRunner {
        // Basic Injection
        private final SpringBeanInjector springBeanInjector;
        private final SpringExtensionPlugin springExtensionPlugin;
        // Spring
        private final ApplicationContext context;
        private final DefaultListableBeanFactory parentBeanFactory = new DefaultListableBeanFactory();
        // DataSource
        private final TransactionManager transactionManager;
        private final DataSourcePlugin dataSourcePlugin;
        private final DataSourcePluginWrapper.CommitMode commitMode;
        // Solr
        private final SolrJPlugin solrJPlugin;

        private SpringRunner(SpringExtensionPlugin springExtensionPlugin) {
            Builder builder = springExtensionPlugin.builder;
            this.springExtensionPlugin = springExtensionPlugin;
            if(builder.dataSourcePluginWrapper != null) {
                this.dataSourcePlugin = builder.dataSourcePluginWrapper.dataSourcePlugin;
                this.transactionManager = builder.dataSourcePluginWrapper.dataSourcePlugin.createTransactionManager();
                this.commitMode = builder.dataSourcePluginWrapper.commitMode;
                if(builder.dataSourcePluginWrapper.resourcesAsSpringBeans){
                    dataSourcePluginResourcesAsSpringBeans();
                }
            }else{
                this.dataSourcePlugin = null;
                this.transactionManager = null;
                this.commitMode = null;
            }

            if(builder.solrJPluginWrapper != null) {
                this.solrJPlugin = builder.solrJPluginWrapper.solrJPlugin;
                if(builder.solrJPluginWrapper.resourcesAsSpringBeans){
                     solrPluginResourcesAsSpringBeans();
                }
            }else{
               this.solrJPlugin = null;
            }

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

        private void solrPluginResourcesAsSpringBeans() {
            parentBeanFactory.registerSingleton("solrSample/SolrClient", solrJPlugin.getClient());
        }

        private void dataSourcePluginResourcesAsSpringBeans() {
            LOG.info("resourceAsSpringBeans for {}", this);
            dataSourcePlugin.getDataSources().forEach(dataSourceProxyInterface -> {
                parentBeanFactory.registerSingleton(dataSourceProxyInterface.getName(), dataSourceProxyInterface);
            });
        }

        private Class<?> getContainerSpringConfigClass() {
            return hasJpaPLugin() ? ContainerAllSpringConfig.class : ContainerSpringConfig.class;
        }

        private boolean hasJpaPLugin() {
            try {
                Class.forName("org.hrodberaht.injection.plugin.junit.plugins.JpaPlugin");
                return dataSourcePlugin != null && dataSourcePlugin instanceof JpaPlugin;
            }catch (ClassNotFoundException e){
                return false;
            }
        }

        private ClassPathXmlApplicationContext loadConfig(String... springConfigs) {
            LOG.info("SpringContainerConfigBase - Creating SpringApplication XML for " + this.getClass());
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
            LOG.info("SpringContainerConfigBase - Creating SpringApplication Java for " + this.getClass());
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

    public static class DataSourcePluginWrapper {

        private enum CommitMode {COMMIT, ROLLBACK}

        private DataSourcePluginWrapper(DataSourcePlugin dataSourcePlugin, Builder builder) {
            this.dataSourcePlugin = dataSourcePlugin;
            this.builder = builder;
        }

        private final DataSourcePlugin dataSourcePlugin;
        private final Builder builder;

        private CommitMode commitMode = CommitMode.ROLLBACK;
        private boolean resourcesAsSpringBeans = false;

        public SpringExtensionPlugin resourceAsSpringBeans() {
            resourcesAsSpringBeans = true;
            return builder.springExtensionPlugin;
        }

        public SpringExtensionPlugin commitAfterContainerCreation() {
            builder.dataSourcePluginWrapper.commitMode = DataSourcePluginWrapper.CommitMode.COMMIT;
            return builder.springExtensionPlugin;
        }
    }

    public static class SolrJPluginWrapper {
        private final SolrJPlugin solrJPlugin;
        private final Builder builder;

        private boolean resourcesAsSpringBeans = false;

        private SolrJPluginWrapper(SolrJPlugin solrJPlugin, Builder builder) {
            this.solrJPlugin = solrJPlugin;
            this.builder = builder;
        }

        public SpringExtensionPlugin resourceAsSpringBeans() {
            LOG.info("resourceAsSpringBeans for {}", this);
            resourcesAsSpringBeans = true;
            return builder.springExtensionPlugin;
        }
    }

    public static class Builder {
        private final SpringExtensionPlugin springExtensionPlugin;
        private String[] springConfigFiles = null;
        private Class[] springConfigsClasses = null;
        private DataSourcePluginWrapper dataSourcePluginWrapper = null;
        private SolrJPluginWrapper solrJPluginWrapper = null;

        public Builder(SpringExtensionPlugin springExtensionPlugin) {
            this.springExtensionPlugin = springExtensionPlugin;
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
            if(springExtensionPlugin.springRunner.springBeanInjector != null) {
                springExtensionPlugin.springRunner.springBeanInjector.inject(service, springExtensionPlugin.injectionRegister.getContainer());
            }
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

    @RunnerPluginBeforeContainerCreation
    protected void beforeContainerCreation(PluginContext pluginContext) {
        springRunner = pluginLifeCycledResource.create(lifeCycle, pluginContext, () -> new SpringRunner(this));
        LOG.info("beforeContainerCreation for {}", this);
        if (springRunner.dataSourcePlugin != null) {
            springRunner.transactionManager.beginTransaction();
        }
    }

    @RunnerPluginAfterContainerCreation
    protected void afterContainerCreation(PluginContext pluginContext) {
        LOG.info("afterContainerCreation for {}", this);
        if (springRunner.dataSourcePlugin != null) {
            if (springRunner.commitMode == DataSourcePluginWrapper.CommitMode.COMMIT) {
                springRunner.transactionManager.endTransactionCommit();
            } else {
                springRunner.transactionManager.endTransaction();
            }
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

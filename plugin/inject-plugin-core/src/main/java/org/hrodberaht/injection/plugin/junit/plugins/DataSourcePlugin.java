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

import org.hrodberaht.injection.core.InjectContainer;
import org.hrodberaht.injection.core.register.InjectionRegister;
import org.hrodberaht.injection.core.spi.JavaResourceCreator;
import org.hrodberaht.injection.core.spi.ResourceFactory;
import org.hrodberaht.injection.plugin.datasource.DatasourceResourceCreator;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.annotation.ResourcePluginFactory;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterContainerCreation;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeTest;
import org.hrodberaht.injection.plugin.junit.datasource.DatasourceContainerService;
import org.hrodberaht.injection.plugin.junit.datasource.ProxyResourceCreator;
import org.hrodberaht.injection.plugin.junit.datasource.TransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataSourcePlugin implements Plugin {

    private final DatasourceResourceCreator datasourceResourceCreator = getDatasourceResourceCreator();
    private static final Map<Class, ResourceRunnerHolder> resourceRunnereState = new ConcurrentHashMap<>();

    private final List<ResourceRunner> beforeSuite = new ArrayList<>();

    private TransactionManager transactionManager;
    private InjectContainer injectContainer;
    private ResourceFactory resourceFactory;


    protected DatasourceResourceCreator getDatasourceResourceCreator() {
        ProxyResourceCreator proxyResourceCreator = new ProxyResourceCreator(
                ProxyResourceCreator.DataSourceProvider.HSQLDB,
                ProxyResourceCreator.DataSourcePersistence.RESTORABLE);
        DatasourceResourceCreator datasourceResourceCreator = new DatasourceResourceCreator(proxyResourceCreator);
        transactionManager = new TransactionManager(proxyResourceCreator);
        return datasourceResourceCreator;
    }

    /**
     * Will load files from the path using a filename pattern matcher
     * First filter is a schema creator pattern as "create_schema_*.sql", example create_schema_user.sql
     * Second filter is a schema creator pattern as "create_schema_*.sql", example create_schema_user.sql
     * The order of the filters are guaranteed to follow create_schema first, insert_script second
     */
    public DataSourcePlugin loadSchema(DataSource dataSource, String classPathRoot) {
        DatasourceContainerService datasourceContainerService = new DatasourceContainerService(dataSource);
        datasourceContainerService.addSQLSchemas("main", classPathRoot);
        return this;
    }

    public DataSourcePlugin loadSchema(String schemaName, DataSource dataSource, String classPathRoot) {
        DatasourceContainerService datasourceContainerService = new DatasourceContainerService(dataSource);
        datasourceContainerService.addSQLSchemas(schemaName, classPathRoot);
        return this;
    }

    @ResourcePluginFactory
    private void setResourceFactory(ResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        this.resourceFactory.addResourceCrator(datasourceResourceCreator);
    }


    @RunnerPluginAfterContainerCreation
    protected void afterContainerCreation(InjectionRegister injectionRegister) {
        this.injectContainer = injectionRegister.getContainer();
        if (!beforeSuite.isEmpty()) {
            transactionManager.beginTransaction();
            beforeSuite.forEach(resourceRunner -> {
                ResourceLoader resourceLoader = new ResourceLoader(resourceRunner);
                resourceRunner.run(resourceLoader);
            });
            transactionManager.endTransactionCommit();
        }
    }

    @RunnerPluginBeforeTest
    protected void beforeTest() {
        transactionManager.beginTransaction();
    }

    @RunnerPluginAfterTest
    protected void afterTest() {
        transactionManager.endTransaction();
    }

    @Override
    public LifeCycle getLifeCycle() {
        return LifeCycle.TEST_SUITE;
    }

    /**
     * This is useful if there is a need to run any code before the actual tests are executed, any results from code executed like this is commited to the underlying datasources and entitymanagers
     *
     * @param runnable the runnable to be added to run before tests start, comparable to @BeforeClass from JUnit, but with a but reusability over testsuites not onlt testclasses
     */
    public DataSourcePlugin addBeforeTestSuite(ResourceRunner runnable) {
        beforeSuite.add(runnable);
        return this;
    }

    public interface ResourceLoaderRunner {
        void run();
    }

    public class ResourceLoader {
        private final ResourceRunner resourceRunner;

        ResourceLoader(ResourceRunner resourceRunner) {
            this.resourceRunner = resourceRunner;
        }

        public ResourceLoaderRunner get(Class<? extends ResourceLoaderRunner> aClass) {
            ResourceRunnerHolder resourceRunnerHolder = resourceRunnereState.get(aClass);
            if (resourceRunnerHolder == null) {
                resourceRunnereState.put(aClass, new ResourceRunnerHolder(resourceRunner));
                return injectContainer.get(aClass);
            }
            return () -> {
            };
        }
    }

    public class ResourceRunnerHolder {
        private final ResourceRunner resourceRunner;
        private boolean hasRun = false;

        public ResourceRunnerHolder(ResourceRunner resourceRunner) {
            this.resourceRunner = resourceRunner;
        }
    }

    public interface ResourceRunner {
        void run(ResourceLoader resourceLoader);
    }


    public <T> JavaResourceCreator<T> getCreator(Class<T> typeClass) {
        return resourceFactory.getCreator(typeClass);
    }
}

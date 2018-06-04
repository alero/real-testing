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

import org.hrodberaht.injection.core.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.plugin.junit.api.annotation.ResourcePluginChainableInjectionProvider;
import org.hrodberaht.injection.plugin.junit.datasource.SimpleDataSourceProxy;
import org.hrodberaht.injection.plugin.junit.jpa.EntityManagerCreator;
import org.hrodberaht.injection.plugin.junit.jpa.EntityManagerHolder;
import org.hrodberaht.injection.plugin.junit.jpa.EntityManagerInjection;
import org.hrodberaht.injection.plugin.junit.resources.ChainableInjectionPointProvider;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

public class JpaPlugin extends DataSourcePlugin {


    private final EntityManagerCreator entityManagerCreator = new EntityManagerCreator();
    private final EntityManagerHolder entityManagerHolder = new EntityManagerHolder();
    private final EntityManagerInjection entityManagerInjection = new EntityManagerInjection();

    public JpaPlugin() {
        super();
    }

    private JpaPlugin(boolean usingJavaContext,
                      LifeCycle lifeCycle,
                      CommitMode commitModeContainerLifeCycle) {
        super(usingJavaContext, lifeCycle, commitModeContainerLifeCycle);
    }

    public EntityManager createEntityManager(DataSource dataSource, String name) {
        try {
            return entityManagerInjection.addPersistenceContext(name, entityManagerCreator.createEntityManager(name));
        } finally {
            SimpleDataSourceProxy testDataSourceWrapper = (SimpleDataSourceProxy) dataSource;
            testDataSourceWrapper.commitDataSource();
        }
    }

    @ResourcePluginChainableInjectionProvider
    protected ChainableInjectionPointProvider createInjectionProvider(InjectionFinder injectionFinder) {
        return new ChainableInjectionPointProvider(injectionFinder) {
            @Override
            public Object extendedInjection(Object service) {
                super.extendedInjection(service);
                entityManagerInjection.injectResources(service);
                return service;
            }
        };
    }

    @Override
    protected void beforeTest() {
        entityManagerHolder.begin(entityManagerCreator.getManagers());
        super.beforeTest();
    }

    @Override
    protected void afterTest() {
        entityManagerHolder.end();
        super.afterTest();
    }

    public static class JpaPluginBuilder extends DataSourcePluginBuilder {

        public JpaPluginBuilder usingJavaContext() {
            super.usingJavaContext();
            return this;
        }

        public JpaPluginBuilder commitAfterContainerCreation() {
            super.commitAfterContainerCreation();
            return this;
        }

        public JpaPlugin build() {
            return new JpaPlugin(usingJavaContext, lifeCycle, commitModeContainerLifeCycle);
        }
    }
}

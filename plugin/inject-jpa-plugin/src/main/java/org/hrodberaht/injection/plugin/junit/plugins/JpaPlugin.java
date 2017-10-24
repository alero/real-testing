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
import org.hrodberaht.injection.plugin.junit.jpa.EntityManagerCreator;
import org.hrodberaht.injection.plugin.junit.jpa.EntityManagerHolder;
import org.hrodberaht.injection.plugin.junit.jpa.EntityManagerInjection;
import org.hrodberaht.injection.plugin.junit.resources.ChainableInjectionPointProvider;

import javax.persistence.EntityManager;

public class JpaPlugin extends DataSourcePlugin {


    private final EntityManagerCreator entityManagerCreator = new EntityManagerCreator();
    private final EntityManagerHolder entityManagerHolder = new EntityManagerHolder();
    private final EntityManagerInjection entityManagerInjection = new EntityManagerInjection();

    public EntityManager createEntityManager(String name) {
        return entityManagerInjection.addPersistenceContext(name, entityManagerCreator.createEntityManager(name));
    }

    @ResourcePluginChainableInjectionProvider
    private ChainableInjectionPointProvider createInjectionProvider(InjectionFinder injectionFinder) {
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
}

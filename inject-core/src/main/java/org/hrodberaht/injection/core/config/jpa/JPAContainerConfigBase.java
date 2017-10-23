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

package org.hrodberaht.injection.core.config.jpa;

import org.hrodberaht.injection.core.config.ContainerConfigBase;
import org.hrodberaht.injection.core.internal.ResourceInjection;
import org.hrodberaht.injection.core.register.InjectionRegister;
import org.hrodberaht.injection.core.spi.ResourceCreator;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Collection;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:35:27
 * @version 1.0
 * @since 1.0
 */
public abstract class JPAContainerConfigBase<T extends InjectionRegister> extends ContainerConfigBase<T> {


    public Collection<EntityManager> getEntityManagers() {
        return resourceInjection.getEntityManagers();
    }

    protected void injectResources(Object serviceInstance) {
        resourceInjection.injectResources(serviceInstance);
    }

    public ResourceCreator<EntityManager, ?> getResourceCreator() {
        return resourceCreator;
    }

    public EntityManager createEntityManager(String schemaName, String dataSourceName, DataSource dataSource) {
        return getResourceCreator().createEntityManager(schemaName, dataSourceName, dataSource);
    }

    public EntityManager getEntityManager(String entityManagerName) {
        return resourceInjection.getEntityManager(entityManagerName);
    }

    @Override
    protected ResourceInjection createResourceInjector() {
        return new ResourceInjection();
    }

    public void addPersistenceContext(String entityManagerName, EntityManager entityManager) {
        resourceInjection.addPersistenceContext(entityManagerName, entityManager);
    }
}

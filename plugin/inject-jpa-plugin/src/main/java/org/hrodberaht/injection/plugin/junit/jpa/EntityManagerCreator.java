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

package org.hrodberaht.injection.plugin.junit.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EntityManagerCreator {

    private static final Logger LOG = LoggerFactory.getLogger(EntityManagerCreator.class);
    private static Map<String, EntityManager> entityManagerMap = new HashMap<>();

    public EntityManager createEntityManager(String name) {

        if (entityManagerMap.get(name) == null) {

            EntityManager entityManager = Persistence.createEntityManagerFactory(name).createEntityManager();
            LOG.info("Created entity manager {}", entityManager);
            entityManagerMap.put(name, entityManager);
            return entityManager;
        }
        EntityManager entityManager = entityManagerMap.get(name);
        LOG.info("Reused entity manager " + entityManager);
        return entityManager;
    }

    public Collection<EntityManager> getManagers() {
        return entityManagerMap.values();
    }
}

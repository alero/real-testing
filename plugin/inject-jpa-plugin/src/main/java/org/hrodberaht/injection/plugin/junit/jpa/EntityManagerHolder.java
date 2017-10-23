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
import java.util.Collection;

public class EntityManagerHolder {

    private static final Logger LOG = LoggerFactory.getLogger(EntityManagerHolder.class);
    private static final ThreadLocal<EntityManagers> MANAGERS = new ThreadLocal<EntityManagers>();


    public void begin(Collection<EntityManager> managers) {
        if (managers != null) {
            MANAGERS.set(new EntityManagers(managers));
            for (EntityManager entityManager : managers) {
                if (entityManager != null) {

                    entityManager.getTransaction().begin();
                    // entityManager.close();
                    LOG.debug("entityManager begin " + entityManager);
                }
            }
        }
    }

    public void end() {
        EntityManagers entityManagers = MANAGERS.get();
        if (entityManagers != null) {
            for (EntityManager entityManager : entityManagers.entityManagers) {
                if (entityManager != null) {

                    entityManager.getTransaction().rollback();
                    entityManager.clear();
                    // entityManager.close();
                    LOG.debug("entityManager rollback " + entityManager);
                }
            }
        }
    }

    public Collection<EntityManager> getEntityManagers() {
        return MANAGERS.get().entityManagers;
    }

    private class EntityManagers {
        private Collection<EntityManager> entityManagers;

        public EntityManagers(Collection<EntityManager> managers) {
            this.entityManagers = managers;
        }
    }
}

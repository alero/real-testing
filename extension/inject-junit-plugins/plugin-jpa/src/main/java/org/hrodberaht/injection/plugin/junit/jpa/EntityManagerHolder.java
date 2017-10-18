package org.hrodberaht.injection.plugin.junit.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.Collection;

/**
 * Created by alexbrob on 2016-03-04.
 */
public class EntityManagerHolder {

    private static final Logger LOG = LoggerFactory.getLogger(EntityManagerHolder.class);
    private static final ThreadLocal<EntityManagers> MANAGERS = new ThreadLocal<EntityManagers>();


    public void begin(Collection<EntityManager> managers) {
        if(managers != null) {
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
        if(entityManagers != null) {
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

    private class EntityManagers{
        private Collection<EntityManager> entityManagers;

        public EntityManagers(Collection<EntityManager> managers) {
            this.entityManagers = managers;
        }
    }
}

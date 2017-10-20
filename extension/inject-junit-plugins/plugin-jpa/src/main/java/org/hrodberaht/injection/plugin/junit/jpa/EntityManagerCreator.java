package org.hrodberaht.injection.plugin.junit.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Makes sure that only one EntityManager is created / JVM to avoid issues with data cleanup/sharing
 */
public class EntityManagerCreator {

    private static final Logger LOG = LoggerFactory.getLogger(EntityManagerCreator.class);
    private static Map<String, EntityManager> entityManagerMap = new HashMap<>();

    public EntityManager createEntityManager(String name) {

        if(entityManagerMap.get(name) == null){

            EntityManager entityManager = Persistence.createEntityManagerFactory(name).createEntityManager();
            LOG.debug("Created entity manager " + entityManager);
            entityManagerMap.put(name, entityManager);
            return entityManager;
        }
        EntityManager entityManager = entityManagerMap.get(name);
        LOG.debug("Reused entity manager " + entityManager);
        return entityManager;
    }

    public Collection<EntityManager> getManagers() {
        return entityManagerMap.values();
    }
}

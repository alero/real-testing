package org.hrodberaht.inject.extension.tdd.internal;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-02-05 19:31
 * @created 1.0
 * @since 1.0
 */
public class EntityManagers {


    private Collection<EntityManager> entityManagers = new ArrayList<EntityManager>();

    public EntityManagers(Collection<EntityManager> entityManager) {
        entityManagers = entityManager;
    }


    public Collection<EntityManager> getEntityManagers() {
        return entityManagers;
    }

    public void setEntityManagers(Collection<EntityManager> entityManagers) {
        this.entityManagers = entityManagers;
    }
}

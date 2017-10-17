package plugins;

import javax.persistence.EntityManager;
import java.util.Collection;

/**
 * Created by alexbrob on 2016-03-04.
 */
class EntityManagerHolder {

    private Collection<EntityManager> entityManagers;

    public EntityManagerHolder(Collection<EntityManager> entityManagers) {
        this.entityManagers = entityManagers;
    }

    public Collection<EntityManager> getEntityManagers() {
        return entityManagers;
    }
}

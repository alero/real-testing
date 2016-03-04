package org.hrodberaht.inject.extension.tdd.util;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Collection;

/**
 * Inject extension JUnit
 *
 * @author Robert Alexandersson
 *         2011-05-03 21:45
 * @created 1.0
 * @since 1.0
 * <p/>
 */
public class JPATestUtil {

    @Inject
    private EntityManagerHolder entityManager;

    public void flushAndClear() {
        Collection<EntityManager> entityManagers =
                entityManager.getEntityManagers();
        for (EntityManager entityManager : entityManagers) {
            entityManager.flush();
            entityManager.clear();
        }
    }
}

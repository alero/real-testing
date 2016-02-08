package org.hrodberaht.inject.extension.tdd;

import org.hrodberaht.inject.extension.tdd.ejb.TDDEJBContainerConfigBase;
import org.hrodberaht.inject.internal.InjectionContainer;
import org.hrodberaht.inject.spi.ThreadConfigHolder;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Collection;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-05-03 21:45
 * @created 1.0
 * @since 1.0
 * <p/>
 * TODO: replace the ThreadConfigHolder with a stateaware injection supporting multiple containers in the same VM and no more need for thread initializers
 */
public class JPATestUtil {

    @Inject
    private InjectionContainer injectionContainer;

    public static void flushAndClear() {
        Collection<EntityManager> entityManagers =
                ((TDDEJBContainerConfigBase) ThreadConfigHolder.get()).getEntityManagers();
        for (EntityManager entityManager : entityManagers) {
            entityManager.flush();
            entityManager.clear();
        }
    }
}

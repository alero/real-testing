package org.hrodberaht.inject.extension.tdd;

import org.hrodberaht.inject.extension.tdd.ejb.TDDEJBContainerConfigBase;
import org.hrodberaht.inject.spi.ContainerConfig;

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
    private ContainerConfig containerConfig;

    public void flushAndClear() {
        Collection<EntityManager> entityManagers =
                ((TDDEJBContainerConfigBase)containerConfig).getEntityManagers();
        for (EntityManager entityManager : entityManagers) {
            entityManager.flush();
            entityManager.clear();
        }
    }
}

package org.hrodberaht.injection.config.jpa;

import org.hrodberaht.injection.config.ContainerConfigBase;
import org.hrodberaht.injection.internal.ResourceInjection;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.spi.ResourceCreator;

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

package org.hrodberaht.injection.plugin.junit.plugins;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.plugin.junit.jpa.EntityManagerCreator;
import org.hrodberaht.injection.plugin.junit.jpa.EntityManagerHolder;

import javax.persistence.EntityManager;

public class JpaPlugin extends DataSourcePlugin {


    private final EntityManagerCreator entityManagerCreator = new EntityManagerCreator();
    private EntityManagerHolder entityManagerHolder = new EntityManagerHolder();

    public EntityManager createEntityManager(String name) {
        return entityManagerCreator.createEntityManager(name);
    }

    @Override
    public void afterContainerCreation(InjectContainer injectContainer) {
        super.afterContainerCreation(injectContainer);
    }

    @Override
    public void beforeMethod(InjectContainer injectContainer) {
        entityManagerHolder.begin(entityManagerCreator.getManagers());
        super.beforeMethod(injectContainer);
    }

    @Override
    public void afterMethod(InjectContainer injectContainer) {
        entityManagerHolder.end();
        super.afterMethod(injectContainer);
    }
}

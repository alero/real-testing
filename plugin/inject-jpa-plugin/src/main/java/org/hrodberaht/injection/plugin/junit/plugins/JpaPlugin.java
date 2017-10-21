package org.hrodberaht.injection.plugin.junit.plugins;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.plugin.junit.jpa.EntityManagerCreator;
import org.hrodberaht.injection.plugin.junit.jpa.EntityManagerHolder;
import org.hrodberaht.injection.plugin.junit.jpa.EntityManagerInjection;
import org.hrodberaht.injection.plugin.junit.resources.ChainableInjectionPointProvider;
import org.hrodberaht.injection.register.InjectionRegister;

import javax.persistence.EntityManager;

public class JpaPlugin extends DataSourcePlugin {


    private final EntityManagerCreator entityManagerCreator = new EntityManagerCreator();
    private final EntityManagerHolder entityManagerHolder = new EntityManagerHolder();
    private final EntityManagerInjection entityManagerInjection = new EntityManagerInjection();

    public EntityManager createEntityManager(String name) {
        return entityManagerInjection.addPersistenceContext(name, entityManagerCreator.createEntityManager(name));
    }

    @Override
    public ChainableInjectionPointProvider getInjectionProvider(InjectionFinder injectionFinder) {
        return new ChainableInjectionPointProvider(injectionFinder) {
            @Override
            public Object extendedInjection(Object service) {
                super.extendedInjection(service);
                entityManagerInjection.injectResources(service);
                return service;
            }
        };
    }

    @Override
    public void afterContainerCreation(InjectionRegister injectionRegister) {
        super.afterContainerCreation(injectionRegister);
    }

    @Override
    public void beforeMethod(InjectionRegister injectionRegister) {
        entityManagerHolder.begin(entityManagerCreator.getManagers());
        super.beforeMethod(injectionRegister);
    }

    @Override
    public void afterMethod(InjectionRegister injectionRegister) {
        entityManagerHolder.end();
        super.afterMethod(injectionRegister);
    }
}

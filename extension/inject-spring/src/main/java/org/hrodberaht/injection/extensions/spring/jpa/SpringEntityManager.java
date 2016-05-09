package org.hrodberaht.injection.extensions.spring.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class SpringEntityManager {


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public PlatformTransactionManager getPlatformTransactionManager() {
        return platformTransactionManager;
    }
}

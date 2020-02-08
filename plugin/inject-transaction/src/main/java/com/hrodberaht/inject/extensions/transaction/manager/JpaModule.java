package com.hrodberaht.inject.extensions.transaction.manager;

import com.hrodberaht.inject.extensions.transaction.TransactionManager;
import com.hrodberaht.inject.extensions.transaction.manager.impl.jpa.TransactionManagerJPAImpl;
import com.hrodberaht.inject.extensions.transaction.manager.util.TransactionManagerUtil;
import org.hrodberaht.injection.core.InjectContainer;
import org.hrodberaht.injection.core.register.ExtendedModule;

import javax.persistence.Persistence;

/**
 * Inject Transactions
 *
 * @author Robert Alexandersson
 * 2010-sep-28 18:24:29
 * @version 1.0
 * @since 1.0
 */
public class JpaModule extends ExtendedModule {

    public JpaModule(String persistenceUnitName) {
        // Create the JPA transaction manager, different managers will need different objects in their construct.
        final TransactionManager transactionManager =
                new TransactionManagerJPAImpl(Persistence.createEntityManagerFactory(persistenceUnitName));
        // Use the special RegistrationModule named TransactionManager,
        // this registers all needed for the container and the service
        // and does a setup for the AspectJTransactionHandler.
        registration = new TransactionManagerModule(transactionManager);

    }

    @Override
    public void postRegistration(InjectContainer injectContainer) {
        TransactionManagerUtil.registerTransactionManager(injectContainer);
    }
}

package com.hrodberaht.inject.extensions.transaction.manager;

import com.hrodberaht.inject.extensions.jdbc.InsertOrUpdater;
import com.hrodberaht.inject.extensions.jdbc.JDBCService;
import com.hrodberaht.inject.extensions.jdbc.internal.InsertOrUpdaterImpl;
import com.hrodberaht.inject.extensions.jdbc.internal.JDBCServiceImpl;
import com.hrodberaht.inject.extensions.transaction.TransactionManager;
import com.hrodberaht.inject.extensions.transaction.manager.impl.jdbc.TransactionManagerJDBC;
import com.hrodberaht.inject.extensions.transaction.manager.impl.jdbc.TransactionManagerJDBCImpl;
import com.hrodberaht.inject.extensions.transaction.manager.impl.jpa.TransactionManagerJPA;
import com.hrodberaht.inject.extensions.transaction.manager.impl.jpa.TransactionManagerJPAImpl;
import com.hrodberaht.inject.extensions.transaction.manager.internal.vendor.ProviderFactory;
import com.hrodberaht.inject.extensions.transaction.manager.internal.vendor.ProviderService;
import org.hrodberaht.injection.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.register.ExtendedModule;
import org.hrodberaht.injection.register.InjectionFactory;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;

import javax.persistence.EntityManager;
import java.sql.Connection;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-11 21:09:52
 * @version 1.0
 * @since 1.0
 */
public class TransactionManagerModule extends ExtendedModule {
    

    public TransactionManagerModule(final TransactionManager transactionManager) {
        registration = new RegistrationModuleAnnotation() {
            @Override
            public void registrations() {
                registerServices(this, transactionManager);
            }

        };
        registration.registrations();
    }

    private void registerServices(RegistrationModuleAnnotation registrationModuleAnnotation, final TransactionManager transactionManager) {
        // the withInstance will automatically create a singleton with that instance
        registrationModuleAnnotation.register(TransactionManager.class).withInstance(transactionManager);

        // This will make the registration to the specific implementation interface as well
        // for usage in an application that needs implementation specific information
        registrationModuleAnnotation.register(getInterface(transactionManager)).withInstance(transactionManager);

        if (transactionManager instanceof InjectionFactory) {
            InjectionFactory injectionFactory = (InjectionFactory) transactionManager;
            registrationModuleAnnotation.register(injectionFactory.getInstanceType()).withFactory(injectionFactory);
        }

        if(transactionManager instanceof TransactionManagerJPA){
            final TransactionManagerJPA transactionManagerJPA = (TransactionManagerJPA)transactionManager;
            InjectionFactory<Connection> injectionFactory = new InjectionFactory<Connection>(){
                @Override
                public Connection getInstance() {
                    return getConnection(transactionManagerJPA);
                }
                @Override
                public Class getInstanceType() {
                    return Connection.class;
                }
                @Override
                public boolean newObjectOnInstance() {
                    return false;
                }
            };
            registrationModuleAnnotation.register(Connection.class).withFactory(injectionFactory);
        }

        // Configure the JDBC Services
        registrationModuleAnnotation.register(JDBCService.class).with(JDBCServiceImpl.class);
        registrationModuleAnnotation.register(InsertOrUpdater.class).with(InsertOrUpdaterImpl.class);
    }

    private Connection getConnection(TransactionManagerJPA transactionManagerJPA) {
        EntityManager entityManager = transactionManagerJPA.getNativeManager();
        ProviderService providerService = ProviderFactory.getService(entityManager);
        if(providerService != null){
            return providerService.findConnection(entityManager);   
        }
        throw new InjectRuntimeException("Cannot find Connection for entity-manager "+entityManager);
    }

    private Class getInterface(TransactionManager transactionManager) {
        if (transactionManager instanceof TransactionManagerJPAImpl) {
            return TransactionManagerJPA.class;
        } else if (transactionManager instanceof TransactionManagerJDBCImpl) {
            return TransactionManagerJDBC.class;
        }
        throw new IllegalArgumentException("transactionManager does not have a specific interface");
    }    

    


}

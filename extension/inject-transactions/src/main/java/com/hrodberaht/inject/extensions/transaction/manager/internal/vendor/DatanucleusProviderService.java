package com.hrodberaht.inject.extensions.transaction.manager.internal.vendor;


import org.datanucleus.store.connection.ManagedConnection;

import javax.persistence.EntityManager;
import java.sql.Connection;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-27 20:54:07
 * @version 1.0
 * @since 1.0
 */
public class DatanucleusProviderService implements ProviderService {
    public Connection findConnection(EntityManager entityManager) {
        org.datanucleus.jpa.EntityManagerImpl entityManagerNative =
                (org.datanucleus.jpa.EntityManagerImpl)entityManager;
        org.datanucleus.ObjectManagerImpl objectManager =
                (org.datanucleus.ObjectManagerImpl) entityManagerNative.getDelegate();
        ManagedConnection managerConnection = 
                objectManager.getStoreManager().getConnection(
                        objectManager.getExecutionContext()
                );
        return (Connection)managerConnection.getConnection() ;
        
    }
}

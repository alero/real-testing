package com.hrodberaht.inject.extensions.transaction.manager.internal.vendor;

import org.eclipse.persistence.internal.databaseaccess.Accessor;
import org.eclipse.persistence.sessions.UnitOfWork;

import javax.persistence.EntityManager;
import java.sql.Connection;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-27 18:07:21
 * @version 1.0
 * @since 1.0
 */
public class EclipseLinkProviderService implements ProviderService {
    public Connection findConnection(EntityManager entityManager) {
        org.eclipse.persistence.internal.jpa.EntityManagerImpl entityManagerEcl =
                (org.eclipse.persistence.internal.jpa.EntityManagerImpl)entityManager;
        UnitOfWork uow = entityManagerEcl.getActiveSession().getActiveUnitOfWork();
        if(uow == null){
            uow = entityManagerEcl.getActiveSession().acquireUnitOfWork();
        }
        Accessor accessor = ((org.eclipse.persistence.internal.sessions.AbstractSession) uow).getAccessor();
        return accessor.getConnection();        

    }
}

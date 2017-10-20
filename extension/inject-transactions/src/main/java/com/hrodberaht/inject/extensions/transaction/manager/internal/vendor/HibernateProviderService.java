package com.hrodberaht.inject.extensions.transaction.manager.internal.vendor;

import javax.persistence.EntityManager;
import java.sql.Connection;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-27 15:08:55
 * @version 1.0
 * @since 1.0
 */
public class HibernateProviderService extends GeneralProviderServiceBase  {
    public Connection findConnection(EntityManager entityManager) {
        //org.hibernate.ejb.HibernateEntityManager entityManagerHib = (org.hibernate.ejb.HibernateEntityManager)entityManager;
        return null;
    }
}

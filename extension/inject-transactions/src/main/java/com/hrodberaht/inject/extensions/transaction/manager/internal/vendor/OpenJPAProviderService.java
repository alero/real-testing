package com.hrodberaht.inject.extensions.transaction.manager.internal.vendor;

import javax.persistence.EntityManager;
import java.sql.Connection;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-27 15:08:55
 * @version 1.0
 * @since 1.0
 */
public class OpenJPAProviderService extends GeneralProviderServiceBase {
    public Connection findConnection(EntityManager entityManager) {
        org.apache.openjpa.persistence.EntityManagerImpl entityManagerNative
                = (org.apache.openjpa.persistence.EntityManagerImpl) entityManager;
        return (Connection) entityManagerNative.getConnection();
    }
}

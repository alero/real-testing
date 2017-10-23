package com.hrodberaht.inject.extensions.transaction.manager.internal.vendor;

import javax.persistence.EntityManager;
import java.sql.Connection;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-27 15:36:22
 * @version 1.0
 * @since 1.0
 */
public abstract class GeneralProviderServiceBase implements ProviderService {
    public Connection getConnection(EntityManager entityManager) {
        //DatabaseLogin login = entityManager.getTransaction();
        //Connection conn = (Connection)login.connectToDataSource(null);
        return null;
    }
}

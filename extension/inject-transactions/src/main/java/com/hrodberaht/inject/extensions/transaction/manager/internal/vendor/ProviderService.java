package com.hrodberaht.inject.extensions.transaction.manager.internal.vendor;

import javax.persistence.EntityManager;
import java.sql.Connection;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-27 15:06:07
 * @version 1.0
 * @since 1.0
 */
public interface ProviderService {
    Connection findConnection(EntityManager entityManager);
}

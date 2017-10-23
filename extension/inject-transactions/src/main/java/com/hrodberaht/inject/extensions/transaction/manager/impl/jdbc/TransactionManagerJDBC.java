package com.hrodberaht.inject.extensions.transaction.manager.impl.jdbc;

import java.sql.Connection;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-21 20:29:06
 * @version 1.0
 * @since 1.0
 */
public interface TransactionManagerJDBC {
    Connection getNativeManager();
}

package com.hrodberaht.inject.extensions.jdbc;

/**
 * JDBC extension
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public interface Insert {

    Insert field(String name, Object value);

}

package com.hrodberaht.inject.extensions.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-21 22:13:42
 * @version 1.0
 * @since 1.0
 */
public interface RowIterator<T> {

    T iterate(ResultSet rs, int iteration) throws SQLException;
    
}

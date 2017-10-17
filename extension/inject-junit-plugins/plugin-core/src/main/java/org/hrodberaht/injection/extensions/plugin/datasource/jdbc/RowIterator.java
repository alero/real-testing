package org.hrodberaht.injection.extensions.plugin.datasource.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Robert Alexandersson
 */
public interface RowIterator<T> {

    T iterate(ResultSet rs, int iteration) throws SQLException;
    
}

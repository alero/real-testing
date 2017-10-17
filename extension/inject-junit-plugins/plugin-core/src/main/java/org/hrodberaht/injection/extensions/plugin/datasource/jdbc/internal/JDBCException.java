package org.hrodberaht.injection.extensions.plugin.datasource.jdbc.internal;

import java.sql.SQLException;

/**
 * @author Robert Alexandersson
 */
public class JDBCException extends RuntimeException {
    public JDBCException(SQLException e) {
        super(e);
    }

    public JDBCException(String message) {
        super(message);
    }

}
package com.hrodberaht.inject.extension.jdbc.internal;

import java.sql.SQLException;

public class JDBCException extends RuntimeException {
    public JDBCException(SQLException e) {
        super(e);
    }

    public JDBCException(String message) {
        super(message);
    }

}
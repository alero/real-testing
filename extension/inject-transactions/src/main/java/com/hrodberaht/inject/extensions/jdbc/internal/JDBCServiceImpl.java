package com.hrodberaht.inject.extensions.jdbc.internal;

import com.hrodberaht.inject.extensions.jdbc.Insert;
import com.hrodberaht.inject.extensions.jdbc.InsertOrUpdater;
import com.hrodberaht.inject.extensions.jdbc.JDBCService;
import com.hrodberaht.inject.extensions.jdbc.RowIterator;
import com.hrodberaht.inject.extensions.jdbc.SQLDateUtil;

import javax.inject.Inject;
import javax.inject.Provider;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-21 22:14:33
 * @version 1.0
 * @since 1.0
 */
public class JDBCServiceImpl implements JDBCService {

    @Inject
    Provider<InsertOrUpdater> iouProvider;

    @Inject
    Provider<Connection> connectionProvider;

    public InsertOrUpdater createInsertOrUpdate(String table) {
        InsertOrUpdater _insertOrUpdater = iouProvider.get();
        InsertOrUpdaterImpl insertOrUpdater = (InsertOrUpdaterImpl) _insertOrUpdater;
        return insertOrUpdater.table(table);
    }

    public Insert createInsert(String table) {
        InsertOrUpdater _insertOrUpdater = iouProvider.get();
        InsertOrUpdaterImpl insertOrUpdater = (InsertOrUpdaterImpl) _insertOrUpdater;
        return insertOrUpdater.table(table);
    }

    public int insert(Insert insert) {
        return insertOrUpdate((InsertOrUpdater) insert);
    }

    public int insertOrUpdate(InsertOrUpdater _insertOrUpdater) {
        try {
            if (_insertOrUpdater instanceof InsertOrUpdaterImpl) {
                InsertOrUpdaterImpl insertOrUpdater = (InsertOrUpdaterImpl) _insertOrUpdater;

                Connection connection = connectionProvider.get();
                PreparedStatement pstmt = null;
                try {
                    String sql = insertOrUpdater.getPreparedSql();
                    pstmt = connection.prepareStatement(sql);
                    Object[] args = insertOrUpdater.getArgs();
                    appendArguments(pstmt, args);
                    return pstmt.executeUpdate();
                } finally {
                    close(pstmt);
                }
            }
        } catch (SQLException e) {
            throw new JDBCException(e);
        }
        throw new RuntimeException("A custom InsertOrUpdater was used, use InsertOrUpdaterImpl is the instance");

    }


    private void appendArguments(PreparedStatement pstmt, Object... args) throws SQLException {
        int argumentOrder = 1;
        for (Object argument : args) {
            addArgument(pstmt, argumentOrder, argument);
            argumentOrder++;
        }
    }

    private void addArgument(PreparedStatement pstmt, int argumentOrder, Object argument) throws SQLException {
        if (argument == null) {
            pstmt.setNull(argumentOrder, java.sql.Types.NULL);
        } else if (argument instanceof String) {
            pstmt.setString(argumentOrder, (String) argument);
        } else if (argument instanceof Long) {
            pstmt.setLong(argumentOrder, (Long) argument);
        } else if (argument instanceof Integer) {
            pstmt.setInt(argumentOrder, (Integer) argument);
        } else if (argument instanceof java.util.Date) {
            if (argument instanceof java.sql.Date) {
                pstmt.setDate(argumentOrder, (java.sql.Date) argument);
            }
            if (argument instanceof Timestamp) {
                pstmt.setTimestamp(argumentOrder, (java.sql.Timestamp) argument);
            } else {
                pstmt.setDate(argumentOrder, new java.sql.Date(((Date) argument).getTime()));
            }
        } else if (argument instanceof Calendar) {
            Calendar cal = (Calendar) argument;
            pstmt.setTimestamp(argumentOrder, SQLDateUtil.getCalendar(cal), cal);
        } else if (argument instanceof BigDecimal) {
            pstmt.setBigDecimal(argumentOrder, (BigDecimal) argument);
        } else if (argument instanceof Byte) {
            pstmt.setByte(argumentOrder, (Byte) argument);
        } else if (argument instanceof Double) {
            pstmt.setDouble(argumentOrder, (Double) argument);
        } else if (argument instanceof Float) {
            pstmt.setFloat(argumentOrder, (Float) argument);
        } else if (argument instanceof Float) {
            pstmt.setFloat(argumentOrder, (Float) argument);
        } else if (argument instanceof Clob) {
            pstmt.setClob(argumentOrder, (Clob) argument);
        } else if (argument instanceof Blob) {
            pstmt.setBlob(argumentOrder, (Blob) argument);
        } else if (argument.getClass().isArray()) {
            // byte arrays are the only supported arrays
            pstmt.setBytes(argumentOrder, (byte[]) argument);
        } else {
            pstmt.setObject(argumentOrder, argument);
        }

    }


    public <T> Collection<T> query(String sql, RowIterator<T> rowIterator, Object... args) {

        try {
            Connection connection = connectionProvider.get();
            // Prepared statements?
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            Collection<T> queryItems = new ArrayList<T>(50);
            try {
                pstmt = prepareAndAppend(sql, connection, args);
                rs = pstmt.executeQuery();
                int iteration = 0;
                while (rs.next()) {
                    T item = rowIterator.iterate(rs, iteration++);
                    queryItems.add(item);
                }
                return queryItems;
            } finally {
                close(pstmt, rs);
            }
        } catch (SQLException e) {
            throw new JDBCException(e);
        }
    }

    public <T> T querySingle(String sql, RowIterator<T> rowIterator, Object... args) {
        try {
            Connection connection = connectionProvider.get();
            // Prepared statements?
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = prepareAndAppend(sql, connection, args);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if (rs.isLast()) {
                        return rowIterator.iterate(rs, 0);
                    }
                    throw new JDBCException("Multiple returns for a query with expected single");
                } else {
                    return null;
                }
            } finally {
                close(pstmt, rs);
            }
        } catch (SQLException e) {
            throw new JDBCException(e);
        }


    }

    public int execute(String sql, Object... args) {
        try {
            Connection connection = connectionProvider.get();
            // Prepared statements?
            PreparedStatement pstmt = null;
            try {
                pstmt = prepareAndAppend(sql, connection, args);
                return pstmt.executeUpdate();
            } finally {
                close(pstmt);
            }
        } catch (SQLException e) {
            throw new JDBCException(e);
        }
    }

    private PreparedStatement prepareAndAppend(String sql, Connection connection, Object[] args) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        appendArguments(pstmt, args);
        return pstmt;
    }


    private void close(Statement stmt, ResultSet rs) {
        close(rs);
        close(stmt);
    }

    private void close(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            // ignore, there is nothing we can do
        }
    }

    private void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            // ignore, there is nothing we can do
        }
    }

}

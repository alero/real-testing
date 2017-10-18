package org.hrodberaht.injection.plugin.datasource.jdbc.internal;

import org.hrodberaht.injection.plugin.datasource.jdbc.*;
import org.hrodberaht.injection.internal.exception.InjectRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * @author Robert Alexandersson
 */
public class JDBCServiceImpl implements JDBCService {

    private static final Logger LOG = LoggerFactory.getLogger(JDBCServiceImpl.class);

    private final InsertOrUpdater insertOrUpdater;
    private final DataSource dataSource;

    public JDBCServiceImpl(InsertOrUpdater insertOrUpdater, DataSource dataSource) {
        this.insertOrUpdater = insertOrUpdater;
        this.dataSource = dataSource;
    }

    @Override
    public InsertOrUpdater createInsertOrUpdate(String table) {
        InsertOrUpdaterImpl insertOrUpdaterImpl = (InsertOrUpdaterImpl) insertOrUpdater;
        return insertOrUpdaterImpl.table(table);
    }

    @Override
    public Insert createInsert(String table) {
        InsertOrUpdaterImpl insertOrUpdaterImpl = (InsertOrUpdaterImpl) insertOrUpdater;
        return insertOrUpdaterImpl.table(table);
    }

    @Override
    public int insert(Insert insert) {
        return insertOrUpdate((InsertOrUpdater) insert);
    }

    @Override
    public int insertOrUpdate(InsertOrUpdater insertOrUpdater) {
        try (Connection connection = dataSource.getConnection()){
            if (insertOrUpdater instanceof InsertOrUpdaterImpl) {
                InsertOrUpdaterImpl insertOrUpdaterImpl = (InsertOrUpdaterImpl) insertOrUpdater;
                PreparedStatement pstmt = null;
                try {
                    String sql = insertOrUpdaterImpl.getPreparedSql();
                    pstmt = connection.prepareStatement(sql);
                    Object[] args = insertOrUpdaterImpl.getArgs();
                    appendArguments(pstmt, args);
                    return pstmt.executeUpdate();
                } finally {
                    close(pstmt);
                }
            }
        } catch (SQLException e) {
            throw new JDBCException(e);
        }
        throw new InjectRuntimeException("A custom InsertOrUpdater was used, use InsertOrUpdaterImpl is the instance");

    }


    @Override
    public <T> T querySingle(String sql, RowIterator<T> rowIterator, Object... args) {
        try {

            // Prepared statements?
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try (Connection connection = dataSource.getConnection()){
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

    @Override
    public int execute(String sql, Object... args) {
        try (Connection connection = dataSource.getConnection()){
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
    @Override
    public <T> Collection<T> query(String sql, RowIterator<T> rowIterator, Object... args) {

        try (Connection connection = dataSource.getConnection()){
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

    private void addArgument(PreparedStatement pstmt, int argumentOrder, Object argument) throws SQLException {
        if (argument == null) {
            pstmt.setNull(argumentOrder, java.sql.Types.NULL);
        } else if (argument instanceof String) {
            pstmt.setString(argumentOrder, (String) argument);
        } else if (argument instanceof Long) {
            pstmt.setLong(argumentOrder, (Long) argument);
        } else if (argument instanceof Integer) {
            pstmt.setInt(argumentOrder, (Integer) argument);
        } else if (argument instanceof Date) {
            addArgumentDate(pstmt, argumentOrder, argument);
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

    private void appendArguments(PreparedStatement pstmt, Object... args) throws SQLException {
        int argumentOrder = 1;
        for (Object argument : args) {
            addArgument(pstmt, argumentOrder, argument);
            argumentOrder++;
        }
    }


    private void addArgumentDate(PreparedStatement pstmt, int argumentOrder, Object argument) throws SQLException {
        if (argument instanceof java.sql.Date) {
            pstmt.setDate(argumentOrder, (java.sql.Date) argument);
        }else if (argument instanceof Timestamp) {
            pstmt.setTimestamp(argumentOrder, (Timestamp) argument);
        } else if(argument instanceof java.sql.Time) {
            pstmt.setTime(argumentOrder, (java.sql.Time) argument);
        }else {
            pstmt.setDate(argumentOrder, new java.sql.Date(((Date) argument).getTime()));
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
            LOG.debug("ignore close failure "+e.getMessage());
        }
    }

    private void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            LOG.debug("ignore close failure "+e.getMessage());
        }
    }

}

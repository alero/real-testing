package com.hrodberaht.inject.extensions.jdbc;

import java.util.Collection;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-21 22:11:56
 * @version 1.0
 * @since 1.0
 */
public interface JDBCService {

    InsertOrUpdater createInsertOrUpdate(String table);
    Insert createInsert(String table);
    
    int insertOrUpdate(InsertOrUpdater insertOrUpdater);
    int insert(Insert insert);

    <T> Collection<T> query(String sql, RowIterator<T> rowIterator, Object... args); 
    <T> T querySingle(String sql, RowIterator<T> rowIterator, Object... args);


    int execute(String sql, Object... args);    
}

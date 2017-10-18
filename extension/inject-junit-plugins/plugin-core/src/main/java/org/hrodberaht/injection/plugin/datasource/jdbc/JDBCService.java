package org.hrodberaht.injection.plugin.datasource.jdbc;

import java.util.Collection;
import java.util.List;

/**
 * @author Robert Alexandersson
 */
public interface JDBCService {

    InsertOrUpdater createInsertOrUpdate(String table);
    Insert createInsert(String table);
    
    int insertOrUpdate(InsertOrUpdater insertOrUpdater);
    int insert(Insert insert);

    <T> List<T> query(String sql, RowIterator<T> rowIterator, Object... args);
    <T> T querySingle(String sql, RowIterator<T> rowIterator, Object... args);


    int execute(String sql, Object... args);    
}

package org.hrodberaht.injection.extensions.plugin.datasource.jdbc;

/**
 * @author Robert Alexandersson
 */
public interface InsertOrUpdater extends Insert{
    
    InsertOrUpdater where(String name, Object value);


}

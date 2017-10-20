package org.hrodberaht.injection.plugin.datasource.jdbc;

import org.hrodberaht.injection.plugin.datasource.jdbc.internal.InsertOrUpdaterImpl;
import org.hrodberaht.injection.plugin.datasource.jdbc.internal.JDBCServiceImpl;

import javax.sql.DataSource;

/**
 * @author Robert Alexandersson
 */
public class JDBCServiceFactory {

    public static JDBCService of(DataSource dataSource) {
        return new JDBCServiceImpl(new InsertOrUpdaterImpl(), dataSource);
    }

}

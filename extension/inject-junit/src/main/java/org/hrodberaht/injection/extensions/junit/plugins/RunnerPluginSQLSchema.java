package org.hrodberaht.injection.extensions.junit.plugins;

import org.hrodberaht.injection.extensions.junit.internal.JunitSQLContainerService;
import org.hrodberaht.injection.extensions.junit.spi.Plugin;

public class RunnerPluginSQLSchema implements Plugin {

    private final JunitSQLContainerService sqlContainerService;
    public RunnerPluginSQLSchema(JunitSQLContainerService sqlContainerService) {
        this.sqlContainerService = sqlContainerService;
    }

    /**
     * Will load files from the path using a filename pattern matcher
     * First filter is a schema creator pattern as "create_schema_*.sql", example create_schema_user.sql
     * Second filter is a schema creator pattern as "create_schema_*.sql", example create_schema_user.sql
     * The order of the filters are guaranteed to follow create_schema first, insert_script second
     */
    public void loadSchema(String dataSourceName, String classPathRoot){
        sqlContainerService.addSQLSchemas(dataSourceName, classPathRoot);
    }

}

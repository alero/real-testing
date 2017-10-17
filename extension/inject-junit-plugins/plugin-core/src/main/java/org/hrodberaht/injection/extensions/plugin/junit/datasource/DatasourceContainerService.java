package org.hrodberaht.injection.extensions.plugin.junit.datasource;

import org.hrodberaht.injection.config.ContainerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * Created by alexbrob on 2016-03-01.
 */
public class DatasourceContainerService {

    private static final Logger LOG = LoggerFactory.getLogger(DatasourceContainerService.class);

    private DataSource dataSource;

    public DatasourceContainerService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addSQLSchemas(String schemaName, String packageBase) {
        DataSourceExecution sourceExecution = new DataSourceExecution(dataSource);
        LOG.debug("JunitSQLContainerService addSQLSchemas " + schemaName + ":" + packageBase);
        if (!sourceExecution.isInitiated(schemaName, packageBase)) {
            LOG.info("addSQLSchemas for " + schemaName + ":" + packageBase);
            sourceExecution.addSQLSchemas(schemaName, packageBase);
        }
    }


    public void addSQLSchemas(String controllerPackageName, String schemaName, String packageBase) {
        DataSourceExecution sourceExecution = new DataSourceExecution(dataSource);
        LOG.debug("JunitSQLContainerService addSQLSchemas " +
                controllerPackageName + ":" + schemaName + ":" + packageBase);
        if (!sourceExecution.isInitiated(controllerPackageName, packageBase)) {
            sourceExecution.addSQLSchemas(schemaName, packageBase);
        }
    }

}

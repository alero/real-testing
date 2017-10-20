package org.hrodberaht.injection.plugin.junit.liquibase;

import liquibase.Liquibase;
import liquibase.database.core.HsqlDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hrodberaht.injection.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.plugin.datasource.DataSourceProxyInterface;
import org.hrodberaht.injection.plugin.datasource.jdbc.JDBCService;
import org.hrodberaht.injection.plugin.datasource.jdbc.JDBCServiceFactory;
import org.hrodberaht.injection.plugin.junit.ResourceWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.sql.SQLException;

class LiquibaseRunner {

    private static final Logger LOG = LoggerFactory.getLogger(LiquibaseRunner.class);

    private final String verificationQuery;
    private final String liquibaseStorageName;
    private final File tempStore;
    private final ResourceWatcher resourceWatcher;

    LiquibaseRunner(String verificationQuery, String liquibaseStorageName, ResourceWatcher resourceWatcher) {
        this.verificationQuery = verificationQuery;
        this.liquibaseStorageName = liquibaseStorageName;
        this.tempStore = new File(this.liquibaseStorageName);
        this.resourceWatcher = resourceWatcher;
    }

    void liquiBaseSchemaCreation(DataSource dataSource, String liquiBaseSchema) throws SQLException, LiquibaseException {

        if (!(dataSource instanceof DataSourceProxyInterface)) {
            throw new InjectRuntimeException("DataSource is not correct for JUnit testing, muse be " + DataSourceProxyInterface.class.getName());
        }

        DataSourceProxyInterface dataSourceProxyInterface = (DataSourceProxyInterface) dataSource;

        Boolean isLoaded = isLoaded(dataSourceProxyInterface);
        if (!isLoaded) {
            loadSchemaDataStore(dataSourceProxyInterface, liquiBaseSchema);
        } else {
            LOG.debug("NOT - RUNNING Liquibase update on schema!");
        }
    }

    private Boolean isLoaded(DataSourceProxyInterface dataSourceProxyInterface) {
        Boolean isLoadedAlready = false;
        try {
            JDBCService jdbcService = JDBCServiceFactory.of(dataSourceProxyInterface);
            isLoadedAlready = jdbcService.querySingle(verificationQuery
                    ,
                    (rs, iteration) -> {
                        rs.next();
                        rs.getString(1);
                        return true;
                    }
            );
        } finally {
            dataSourceProxyInterface.clearDataSource();
        }
        return isLoadedAlready == null ? false : isLoadedAlready;
    }

    private void loadSchemaDataStore(DataSourceProxyInterface dataSource, String liquiBaseSchema) throws SQLException, LiquibaseException {

        if (isTempStoreValid()) {
            readFromFile(dataSource);
        } else {
            loadSchemaFromConfig(dataSource, liquiBaseSchema);
            if (tempStore.exists()) {
                if (!tempStore.delete()) {
                    LOG.debug("could not delete " + tempStore.getPath());
                }
            }
            dataSource.createSnapshot(liquibaseStorageName);
        }
    }

    private void readFromFile(DataSourceProxyInterface dataSource) {
        dataSource.loadSnapshot(liquibaseStorageName);
    }

    private boolean isTempStoreValid() {
        return tempStore.exists() && !resourceWatcher.hasChanged();
    }


    private void loadSchemaFromConfig(DataSourceProxyInterface dataSource, String liquiBaseSchema) throws SQLException, LiquibaseException {

        DataSourceProxyInterface dataSourceProxyInterface = init(dataSource);

        try {
            dataSourceProxyInterface.runWithConnectionAndCommit(con -> {
                        LOG.debug("RUNNING Liquidbase update on schema!");
                        try {
                            HsqlDatabase hsqlDatabase = new HsqlDatabase() {
                                // This feature exists in a PR against liquibase
                                public boolean failOnDefferable() {
                                    return false;
                                }
                            };
                            hsqlDatabase.setConnection(new JdbcConnection(con));
                            Liquibase liquibase = new Liquibase(liquiBaseSchema,
                                    new ClassLoaderResourceAccessor(), hsqlDatabase);
                            liquibase.update("");
                        } catch (LiquibaseException e) {
                            throw new RuntimeException(e);
                        }
                        return true;
                    }
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private DataSourceProxyInterface init(DataSourceProxyInterface dataSource) throws SQLException {
        // Connection has to be called to initialize the proxy to be able to call getNativeConnection
        dataSource.getConnection();
        return dataSource;
    }

}

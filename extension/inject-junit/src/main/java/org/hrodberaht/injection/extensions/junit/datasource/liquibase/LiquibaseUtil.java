package org.hrodberaht.injection.extensions.junit.datasource.liquibase;

import liquibase.Liquibase;
import liquibase.database.core.HsqlDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hrodberaht.injection.extensions.junit.internal.TDDLogger;
import org.hrodberaht.injection.extensions.junit.internal.embedded.ResourceWatcher;
import org.hrodberaht.injection.spi.DataSourceProxyInterface;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class LiquibaseUtil {

    private final String verificationQuery;
    private final String liquibaseStorageName;
    private final File tempStore;
    private final ResourceWatcher resourceWatcher;

    public LiquibaseUtil(String verificationQuery, String liquibaseStorageName, ResourceWatcher resourceWatcher) {
        this.verificationQuery = verificationQuery;
        this.liquibaseStorageName = liquibaseStorageName;
        this.tempStore = new File(this.liquibaseStorageName);
        this.resourceWatcher = resourceWatcher;
    }

    public void liquiBaseSchemaCreation(DataSource dataSource, String liquiBaseSchema) throws SQLException, LiquibaseException {

        if (!(dataSource instanceof DataSourceProxyInterface)) {
            throw new RuntimeException("DataSource is not correct for JUnit testing, muse be " + DataSourceProxyInterface.class.getName());
        }

        DataSourceProxyInterface dataSourceProxyInterface = (DataSourceProxyInterface) dataSource;

        Boolean isLoadedAlready = verifyLoading(dataSourceProxyInterface);
        if (isLoadedAlready) {
            loadSchemaDataStore(dataSourceProxyInterface, liquiBaseSchema);
        } else {
            TDDLogger.log("NOT - RUNNING Liquibase update on schema!");
        }
    }

    private Boolean verifyLoading(DataSourceProxyInterface dataSourceProxyInterface) {
        Boolean isLoadedAlready;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceProxyInterface);
            isLoadedAlready = jdbcTemplate.query(verificationQuery
                    ,
                    resultSet -> {
                        try {
                            resultSet.next();
                            resultSet.getString(1);
                            return false;
                        } catch (Throwable e) {
                            return true;
                        }
                    }
            );
        } finally {
            dataSourceProxyInterface.clearDataSource();

        }
        return isLoadedAlready;
    }

    private void loadSchemaDataStore(DataSourceProxyInterface dataSource, String liquiBaseSchema) throws SQLException, LiquibaseException {

        if (isTempStoreValid()) {
            readFromFile(dataSource);
        } else {
            loadSchemaFromConfig(dataSource, liquiBaseSchema);
            if (tempStore.exists()) {
                tempStore.delete();
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
                TDDLogger.log("RUNNING Liquidbase update on schema!");
                    try{
                        HsqlDatabase hsqlDatabase = new HsqlDatabase() {
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

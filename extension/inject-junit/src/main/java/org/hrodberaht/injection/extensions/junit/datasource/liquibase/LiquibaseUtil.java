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

    private String verificationQuery;
    private File tempStore;
    private ResourceWatcher resourceWatcher;

    public LiquibaseUtil(String verificationQuery, File file, ResourceWatcher resourceWatcher) {
        this.verificationQuery = verificationQuery;
        this.tempStore = file;
        this.resourceWatcher = resourceWatcher;
    }

    public void liquiBaseSchemaCreation(DataSource dataSource, String liquiBaseSchema) throws SQLException, LiquibaseException {

        if (!(dataSource instanceof DataSourceProxyInterface)) {
            throw new RuntimeException("Datasource is not correct for JUnit testing, muse be " + DataSourceProxyInterface.class.getName());
        }

        DataSourceProxyInterface dataSourceProxyInterface = (DataSourceProxyInterface) dataSource;

        Boolean isLoadedAlready = verifyLoading(dataSourceProxyInterface);
        if (isLoadedAlready) {
            loadSchemaDataStore(dataSourceProxyInterface, liquiBaseSchema);
        } else {
            TDDLogger.log("NOT - RUNNING Liquidbase update on schema!");
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
            dataSource.createSnapshot();
        }
    }

    private void readFromFile(DataSourceProxyInterface dataSource) {
        dataSource.loadSnapshot();
    }

    private boolean isTempStoreValid() {
        return tempStore.exists() && !resourceWatcher.hasChanged();
    }


    private void loadSchemaFromConfig(DataSourceProxyInterface dataSource, String liquiBaseSchema) throws SQLException, LiquibaseException {

        DataSourceProxyInterface dataSourceProxyInterface = init(dataSource);
        try (
                Connection connection = dataSourceProxyInterface.getNativeConnection();
        ) {

            TDDLogger.log("RUNNING Liquidbase update on schema!");

            HsqlDatabase hsqlDatabase = new HsqlDatabase() {
                @Override
                public boolean failOnDefferable() {
                    return false;
                }
            };
            hsqlDatabase.setConnection(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(liquiBaseSchema,
                    new ClassLoaderResourceAccessor(), hsqlDatabase);

            liquibase.update("");


        } finally {
            dataSourceProxyInterface.commitDataSource();
        }

    }

    private DataSourceProxyInterface init(DataSourceProxyInterface dataSource) throws SQLException {
        // Connection has to be called to initialize the proxy to be able to call getNativeConnection
        dataSource.getConnection();
        return dataSource;
    }

}

package org.hrodberaht.injection.plugin.junit.plugins;

import liquibase.exception.LiquibaseException;
import org.hrodberaht.injection.plugin.junit.liquibase.LiquibaseManager;
import org.hrodberaht.injection.plugin.junit.spi.Plugin;

import javax.sql.DataSource;
import java.sql.SQLException;

public class LiquibasePlugin extends DataSourcePlugin implements Plugin {

    private String name = "main";

    public LiquibasePlugin name(String name) {
        this.name = name;
        return this;
    }

    /**
     * The liquibaseSchema should point to a single "master" loader file for the liquibase schema that exists in the
     *
     * @param liquibaseSchema the classpath location of the  liquibase schema, sample""
     * @param watchers        a list of files that will be watched for change between runs and keep the snapshot store up to date
     */
    public LiquibasePlugin load(DataSource dataSource, String liquibaseSchema, String... watchers) {
        String query = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'DATABASECHANGELOG'";
        LiquibaseManager liquibaseManager = new LiquibaseManager(name, query);
        try {
            liquibaseManager.liquiBaseSchemaCreation(dataSource, liquibaseSchema, watchers);
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public LifeCycle getLifeCycle() {
        return LifeCycle.SINGLETON;
    }
}

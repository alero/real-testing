package org.hrodberaht.injection.plugin.junit.plugins;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.plugin.datasource.DatasourceResourceCreator;
import org.hrodberaht.injection.plugin.junit.datasource.DatasourceContainerService;
import org.hrodberaht.injection.plugin.junit.datasource.TransactionManager;
import org.hrodberaht.injection.plugin.junit.resources.PluggableResourceFactory;
import org.hrodberaht.injection.plugin.junit.spi.Plugin;
import org.hrodberaht.injection.plugin.junit.spi.ResourcePlugin;
import org.hrodberaht.injection.plugin.junit.datasource.ProxyResourceCreator;
import org.hrodberaht.injection.plugin.junit.spi.RunnerPlugin;
import org.hrodberaht.injection.spi.JavaResourceCreator;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

public class DataSourcePlugin implements RunnerPlugin, ResourcePlugin {

    private final DatasourceResourceCreator datasourceResourceCreator = getDatasourceResourceCreator();
    private final List<Class> classList = Arrays.asList(datasourceResourceCreator.getType());
    private final String DEFAULT_SCHEMA_NAME = "main";

    private PluggableResourceFactory pluggableResourceFactory;
    private TransactionManager transactionManager;

    private DatasourceResourceCreator getDatasourceResourceCreator() {
        ProxyResourceCreator proxyResourceCreator = new ProxyResourceCreator(
                ProxyResourceCreator.DataSourceProvider.HSQLDB,
                ProxyResourceCreator.DataSourcePersistence.RESTORABLE);
        DatasourceResourceCreator datasourceResourceCreator = new DatasourceResourceCreator(proxyResourceCreator);
        transactionManager = new TransactionManager(proxyResourceCreator);
        return datasourceResourceCreator;
    }

    /**
     * Will load files from the path using a filename pattern matcher
     * First filter is a schema creator pattern as "create_schema_*.sql", example create_schema_user.sql
     * Second filter is a schema creator pattern as "create_schema_*.sql", example create_schema_user.sql
     * The order of the filters are guaranteed to follow create_schema first, insert_script second
     */
    public DataSourcePlugin loadSchema(DataSource dataSource, String classPathRoot){
        DatasourceContainerService datasourceContainerService = new DatasourceContainerService(dataSource);
        datasourceContainerService.addSQLSchemas(DEFAULT_SCHEMA_NAME, classPathRoot);
        return this;
    }

    public DataSourcePlugin loadSchema(String schemaName, DataSource dataSource, String classPathRoot){
        DatasourceContainerService datasourceContainerService = new DatasourceContainerService(dataSource);
        datasourceContainerService.addSQLSchemas(schemaName, classPathRoot);
        return this;
    }

    @Override
    public List<Class> getCustomTypes() {
        return classList;
    }

    @Override
    public <T> JavaResourceCreator<T> getCreator(Class<T> aClass) {
        return pluggableResourceFactory.getCreator(aClass);
    }

    @Override
    public <T> JavaResourceCreator<T> getInnerCreator(Class<T> aClass) {
        return (JavaResourceCreator<T>) datasourceResourceCreator;
    }

    @Override
    public void setPluggableResourceFactory(PluggableResourceFactory pluggableResourceFactory) {
        this.pluggableResourceFactory = pluggableResourceFactory;
    }

    @Override
    public void beforeContainerCreation() {

    }

    @Override
    public void afterContainerCreation(InjectContainer injectContainer) {

    }

    @Override
    public void beforeMethod(InjectContainer injectContainer) {
        transactionManager.beginTransaction();
    }

    @Override
    public void afterMethod(InjectContainer injectContainer) {
        transactionManager.endTransaction();
    }

    @Override
    public LifeCycle getLifeCycle() {
        return LifeCycle.NEW;
    }
}

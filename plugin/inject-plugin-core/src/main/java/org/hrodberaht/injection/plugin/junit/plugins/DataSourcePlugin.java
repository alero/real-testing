package org.hrodberaht.injection.plugin.junit.plugins;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.plugin.datasource.DatasourceResourceCreator;
import org.hrodberaht.injection.plugin.junit.datasource.DatasourceContainerService;
import org.hrodberaht.injection.plugin.junit.datasource.ProxyResourceCreator;
import org.hrodberaht.injection.plugin.junit.datasource.TransactionManager;
import org.hrodberaht.injection.plugin.junit.spi.Plugin;
import org.hrodberaht.injection.plugin.junit.spi.annotation.ResourcePluginFactory;
import org.hrodberaht.injection.plugin.junit.spi.annotation.RunnerPluginAfterContainerCreation;
import org.hrodberaht.injection.plugin.junit.spi.annotation.RunnerPluginAfterTest;
import org.hrodberaht.injection.plugin.junit.spi.annotation.RunnerPluginBeforeTest;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.spi.JavaResourceCreator;
import org.hrodberaht.injection.spi.ResourceFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class DataSourcePlugin implements Plugin {

    private final DatasourceResourceCreator datasourceResourceCreator = getDatasourceResourceCreator();
    private final List<Runnable> beforeSuite = new ArrayList<>();

    private TransactionManager transactionManager;
    private InjectContainer injectContainer;
    private ResourceFactory resourceFactory;


    protected DatasourceResourceCreator getDatasourceResourceCreator() {
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
    public DataSourcePlugin loadSchema(DataSource dataSource, String classPathRoot) {
        DatasourceContainerService datasourceContainerService = new DatasourceContainerService(dataSource);
        datasourceContainerService.addSQLSchemas("main", classPathRoot);
        return this;
    }

    public DataSourcePlugin loadSchema(String schemaName, DataSource dataSource, String classPathRoot) {
        DatasourceContainerService datasourceContainerService = new DatasourceContainerService(dataSource);
        datasourceContainerService.addSQLSchemas(schemaName, classPathRoot);
        return this;
    }

    @ResourcePluginFactory
    private void setResourceFactory(ResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        this.resourceFactory.addResourceCrator(datasourceResourceCreator);
    }


    @RunnerPluginAfterContainerCreation
    protected void afterContainerCreation(InjectionRegister injectionRegister) {
        this.injectContainer = injectionRegister.getContainer();
        if (this.beforeSuite.size() > 0) {
            transactionManager.beginTransaction();
            this.beforeSuite.forEach(Runnable::run);
            transactionManager.endTransactionCommit();
        }
    }


    @RunnerPluginBeforeTest
    protected void beforeTest() {
        transactionManager.beginTransaction();
    }

    @RunnerPluginAfterTest
    protected void afterTest() {
        transactionManager.endTransaction();
    }

    @Override
    public LifeCycle getLifeCycle() {
        return LifeCycle.TEST_SUITE;
    }

    /**
     * This is useful if there is a need to run any code before the actual tests are executed, any results from code executed like this is commited to the underlying datasources and entitymanagers
     *
     * @param runnable the runnable to be added to run before tests start, comparable to @BeforeClass from JUnit, but with a but reusability over testsuites not onlt testclasses
     */
    public DataSourcePlugin addBeforeTestSuite(Runnable runnable) {
        this.beforeSuite.add(runnable);
        return this;
    }

    public <T> T getService(Class<T> aClass) {
        return injectContainer.get(aClass);
    }

    public <T> JavaResourceCreator<T> getCreator(Class<T> typeClass) {
        return resourceFactory.getCreator(typeClass);
    }
}

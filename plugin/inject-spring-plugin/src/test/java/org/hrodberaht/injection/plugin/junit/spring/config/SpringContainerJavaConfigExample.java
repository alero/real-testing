package org.hrodberaht.injection.plugin.junit.spring.config;

import org.hrodberaht.injection.plugin.junit.PluggableContainerConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.DataSourcePlugin;
import org.hrodberaht.injection.plugin.junit.plugins.SpringExtensionPlugin;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

import javax.sql.DataSource;

import static org.hrodberaht.injection.plugin.junit.spring.config.SpringContainerConfigExample._package;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:37:42
 * @version 2.0
 * @since 2.0
 */
public class SpringContainerJavaConfigExample extends PluggableContainerConfigBase {


    public SpringContainerJavaConfigExample() {
        // Prepare the datasource
        /*
        String dataSourceName = "jdbc/MyDataSource";
        DataSource dataSource = createDataSource(dataSourceName);
        addResource(dataSourceName, dataSource);

        // Load reusable test data to the datasource
        junitSQLContainerService = new JunitSQLContainerService(this);
        junitSQLContainerService.addSQLSchemas(dataSourceName, "sql");
        junitSQLContainerService.addSQLSchemas(dataSourceName, "sql/insert");

        // Adding the spring config, will combine the IoC of the tests and the spring config
        loadJavaSpringConfig(SpringConfigJavaSample.class);
        */
    }

    @Override
    protected void register(InjectionRegistryBuilder registryBuilder) {
        String dataSourceName = "MyDataSource";
        DataSourcePlugin dataSourcePlugin = activatePlugin(DataSourcePlugin.class);
        DataSource dataSource = dataSourcePlugin.getCreator(DataSource.class).create(dataSourceName);

        dataSourcePlugin.loadSchema(dataSource, "sql");
        dataSourcePlugin.loadSchema(dataSource, "sql/insert");

        activatePlugin(SpringExtensionPlugin.class).loadConfig(SpringConfigJavaSample.class);

        registryBuilder.scan(() -> _package);
    }

    /*
    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("org.hrodberaht.injection.extensions.spring.testservices.simple");
    }
    */

}

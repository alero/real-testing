package org.hrodberaht.injection.extensions.spring.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.junit.internal.JunitSQLContainerService;
import org.hrodberaht.injection.extensions.junit.internal.ProxyResourceCreator;
import org.hrodberaht.injection.extensions.spring.SpringContainerConfigBase;
import org.hrodberaht.injection.extensions.spring.junit.SpringBeanReplacementProxy;
import org.hrodberaht.injection.spi.ResourceCreator;

import javax.sql.DataSource;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:37:42
 * @version 2.0
 * @since 2.0
 */
public class SpringContainerJavaConfigComboExample extends SpringContainerConfigBase {

    private JunitSQLContainerService junitSQLContainerService;

    public SpringContainerJavaConfigComboExample() {
        // Prepare the datasource
        String dataSourceName = "jdbc/MyDataSource";
        DataSource dataSource = createDataSource(dataSourceName);
        addResource(dataSourceName, dataSource);

        // Load reusable test data to the datasource
        junitSQLContainerService = new JunitSQLContainerService(this);
        junitSQLContainerService.addSQLSchemas(dataSourceName, "sql");
        junitSQLContainerService.addSQLSchemas(dataSourceName, "sql/insert");

        // Adding the spring config, will combine the IoC of the tests and the spring config
        loadJavaSpringConfig(SpringConfigJavaComboSample.class,
                SpringBeanReplacementProxy.class);

    }

    @Override
    public void addSingletonActiveRegistry() {
        junitSQLContainerService.addSingletonActiveEntityManagers();
        super.addSingletonActiveRegistry();
    }

    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("org.hrodberaht.injection.extensions.spring.testservices.simple");
    }

    @Override
    protected ResourceCreator createResourceCreator() {
        return new ProxyResourceCreator();
    }
}

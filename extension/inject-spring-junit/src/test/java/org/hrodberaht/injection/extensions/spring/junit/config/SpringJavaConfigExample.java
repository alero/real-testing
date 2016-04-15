package org.hrodberaht.injection.extensions.spring.junit.config;

import org.hrodberaht.injection.extensions.spring.junit.JUnitSpringContainerConfigBase;

import javax.sql.DataSource;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:37:42
 * @version 2.0
 * @since 2.0
 */
public class SpringJavaConfigExample extends JUnitSpringContainerConfigBase {

    public SpringJavaConfigExample() {


        // Prepare the datasource
        String dataSourceName = "jdbc/MyDataSource";
        DataSource dataSource = createDataSource(dataSourceName);
        addResource(dataSourceName, dataSource);

        // Load reusable test data to the datasource
        junitSQLContainerService.addSQLSchemas(dataSourceName, "sql");

        // Adding the spring config, will combine the IoC of the tests and the spring config
        loadJavaSpringConfig(SpringConfigJavaSample.class);

    }


}

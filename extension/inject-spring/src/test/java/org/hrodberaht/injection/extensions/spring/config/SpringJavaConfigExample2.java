package org.hrodberaht.injection.extensions.spring.config;


import org.hrodberaht.injection.extensions.spring.base.JUnitSpringContainerConfigBase;

import javax.sql.DataSource;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:37:42
 * @version 2.0
 * @since 2.0
 */
public class SpringJavaConfigExample2 extends JUnitSpringContainerConfigBase {

    public SpringJavaConfigExample2() {
        final String dataSourceName = "jdbc/MyDataSource";

        /*

        new SpringInjectionRegistryStream(this)
            .resource(injectionResources -> { createDataSource(dataSourceName); })
            .springConfig(() -> SpringConfigJavaSample2.class )
        ;
*/

        // Prepare the datasource

        DataSource dataSource = createDataSource(dataSourceName);
        addResource(dataSourceName, dataSource);

        // Load reusable test data to the datasource

        junitSQLContainerService.addSQLSchemas(dataSourceName, "sql");
        junitSQLContainerService.addSQLSchemas(dataSourceName, "sql/insert");

        // Adding the spring config, will combine the IoC of the tests and the spring config
        loadJavaSpringConfig(SpringConfigJavaSample2.class);

    }


}

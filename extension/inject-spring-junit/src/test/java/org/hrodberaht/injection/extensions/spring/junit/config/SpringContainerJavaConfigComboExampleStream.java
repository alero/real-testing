package org.hrodberaht.injection.extensions.spring.junit.config;

import org.hrodberaht.injection.extensions.spring.junit.JunitSpringStreamApplication;
import org.hrodberaht.injection.extensions.spring.junit.SpringBeanReplacementProxy;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:37:42
 * @version 2.0
 * @since 2.0
 */
public class SpringContainerJavaConfigComboExampleStream extends JunitSpringStreamApplication {

    // private JunitSQLContainerService junitSQLContainerService;

    public SpringContainerJavaConfigComboExampleStream() {
        String dataSourceName = "jdbc/MyDataSource";

        stream()
                .scan(() -> "org.hrodberaht.injection.extensions.spring.testservices")
                .resource(e -> e.createDataSource(dataSourceName))
                .addSQLSchemas(e -> {
                    e.setPath("sql");
                    e.setSchema(dataSourceName);
                })
                // .addSQLSchemas(e -> { e.setPath("sql/insert"); e.setSchema(dataSourceName);})
                .springConfig(configResource -> configResource.config(
                        SpringConfigJavaComboSample.class,
                        SpringBeanReplacementProxy.class)
                )
        ;

        // add();
    }


}

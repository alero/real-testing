package org.hrodberaht.injection.extensions.plugin.junit.demo2.test.config;

import org.hrodberaht.injection.extensions.plugin.junit.PluggableContainerConfigBase;
import org.hrodberaht.injection.extensions.plugin.junit.demo2.service.MyResource;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-05-03 20:31
 * @created 1.0
 * @since 1.0
 */
public class Course2ContainerConfigExample extends PluggableContainerConfigBase {

    public Course2ContainerConfigExample() {

/*        String dataSourceName = "MyDataSource";
        DataSource dataSource = super.createDataSource(dataSourceName);
        super.addResource(dataSourceName, dataSource);

        super.addSQLSchemas(
                "Course2ContainerConfigExample", "MyDataSource", "org/hrodberaht/injection/extensions/course2"
        );
        */
    }



    @Override
    public void register(InjectionRegistryBuilder registryBuilder) {
        registryBuilder
            .scan(() -> "org.hrodberaht.injection.extensions.plugin.junit.demo2.service")
            .resource(builder ->
                    builder
                            .resource("myResource", new MyResource("named"))
                            .resource("myMappedResource", new MyResource("mapped"))
                            .resource(new MyResource("typed"))

            )
        ;
    }
}

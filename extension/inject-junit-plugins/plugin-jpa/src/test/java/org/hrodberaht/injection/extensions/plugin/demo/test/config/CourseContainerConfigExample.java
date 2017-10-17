package org.hrodberaht.injection.extensions.plugin.demo.test.config;

import org.hrodberaht.injection.extensions.plugin.test.PluggableContainerConfigBase;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;
import plugins.JpaPlugin;

import javax.sql.DataSource;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-05-03 20:31
 * @created 1.0
 * @since 1.0
 */
public class CourseContainerConfigExample extends PluggableContainerConfigBase {

    public CourseContainerConfigExample() {

        String dataSourceName = "MyDataSource";
        String jpaName = "example-jpa";
        /*
        DataSource dataSource = createDataSource(dataSourceName);
        // EntityManager resource
        EntityManager entityManager = createEntityManager(jpaName, dataSourceName, dataSource);
        addPersistenceContext(jpaName, entityManager);

        addSQLSchemas(
                "CourseContainerConfigExample", "MyDataSource", "org/hrodberaht/injection/extensions/course");
        */
    }


    @Override
    protected void register(InjectionRegistryBuilder registryBuilder) {
        JpaPlugin dataSourcePlugin = activatePlugin(JpaPlugin.class);

        DataSource dataSource = dataSourcePlugin.getCreator(DataSource.class).create("MyDataSource");

        // DataSource dataSourceTwo = getCreator(DataSource.class).create("secondDataSource");

        // Load schema is a custom method located in the plugin code, this creates clean separation
        dataSourcePlugin
                .loadSchema(dataSource, "org.hrodberaht.injection.extensions.plugin.course")
        // .loadSchema(dataSource, "org.hrodberaht.injection.extensions.plugin.course2")
        ;


        registryBuilder
                .scan(() -> "org.hrodberaht.injection.extensions.plugin.demo")
                .resource(builder ->
                        builder
                                .resource("MyDataSource", DataSource.class, dataSource)

                )
        ;
    }
}

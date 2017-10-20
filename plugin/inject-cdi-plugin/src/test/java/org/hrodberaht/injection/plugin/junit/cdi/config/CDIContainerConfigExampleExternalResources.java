package org.hrodberaht.injection.plugin.junit.cdi.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.plugin.junit.PluggableContainerConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.CDIInjectionPlugin;
import org.hrodberaht.injection.plugin.junit.plugins.JpaPlugin;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:37:42
 * @version 1.0
 * @since 1.0
 */
public class CDIContainerConfigExampleExternalResources extends PluggableContainerConfigBase {
    @Override
    protected void register(InjectionRegistryBuilder injectionRegistryBuilder) {
        activatePlugin(CDIInjectionPlugin.class);
        JpaPlugin jpaPlugin = activatePlugin(JpaPlugin.class);
        DataSource dataSource = jpaPlugin.getCreator(DataSource.class).create("ExampleDataSource");
        jpaPlugin.createEntityManager("example-jpa");



        injectionRegistryBuilder
                .scan(() -> "org.hrodberaht.injection.extensions.cdi.example.service")
        .resource(builder -> builder.resource("ExampleDataSource", dataSource))
        ;
    }

    /*
    public CDIContainerConfigExampleExternalResources() {

        String dataSourceName = "ExampleDataSource";
        String jpaName = "example-jpa";
        DataSource dataSource = createDataSource(dataSourceName);
        // Named resource
        addResource(dataSourceName, dataSource);

        EntityManager entityManager = createEntityManager(jpaName, dataSourceName, dataSource);
        addPersistenceContext(jpaName, entityManager);

    }

    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("org.hrodberaht.injection.extensions.cdi.example.service");
    }
    */


}

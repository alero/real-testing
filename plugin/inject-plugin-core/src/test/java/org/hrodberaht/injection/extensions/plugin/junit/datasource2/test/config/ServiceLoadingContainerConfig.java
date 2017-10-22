package org.hrodberaht.injection.extensions.plugin.junit.datasource2.test.config;

import org.hrodberaht.injection.extensions.plugin.junit.datasource2.test.LoadingTheTestWithData;
import org.hrodberaht.injection.plugin.junit.PluggableContainerConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.DataSourcePlugin;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

import javax.sql.DataSource;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 * 2011-05-03 20:31
 * @created 1.0
 * @since 1.0
 */
public class ServiceLoadingContainerConfig extends PluggableContainerConfigBase {


    @Override
    public void register(InjectionRegistryBuilder registryBuilder) {

        DataSourcePlugin dataSourcePlugin = activatePlugin(DataSourcePlugin.class);

        DataSource dataSource = dataSourcePlugin.getCreator(DataSource.class).create("MyDataSource2");

        // Load schema is a custom method located in the plugin code, this creates clean separation
        dataSourcePlugin
                .loadSchema(dataSource, "org.hrodberaht.injection.extensions.plugin.course")
                .addBeforeTestSuite(() -> dataSourcePlugin.getService(LoadingTheTestWithData.class).load())
        ;


        registryBuilder
                .scan(() -> "org.hrodberaht.injection.extensions.plugin.junit.datasource2.service")
                .resource(builder ->
                        builder
                                // .bindPluginResources()
                                .resource("MyDataSource2", DataSource.class, dataSource)
                                .resource(DataSource.class, dataSource)

                )
        ;
    }


}

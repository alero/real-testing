package org.hrodberaht.injection.plugin.junit.cdi.config.ioc;

import org.hrodberaht.injection.Module;
import org.hrodberaht.injection.plugin.junit.plugins.DataSourcePlugin;
import org.hrodberaht.injection.plugin.junit.plugins.JpaPlugin;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

import javax.sql.DataSource;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class ExampleModuleExternal {

    public static final String DATASOURCE = "ExampleDataSource";

    private Module module;

    public ExampleModuleExternal(InjectionRegistryBuilder configBase, JpaPlugin jpaPlugin) {
        DataSource dataSource = jpaPlugin.getCreator(DataSource.class).create(DATASOURCE);
        jpaPlugin.createEntityManager("example-jpa");
        module = configBase
                .scan(() -> "org.hrodberaht.injection.extensions.cdi.example.service")
                .resource(builder -> builder.resource(DATASOURCE, dataSource))
                .getModule();
    }

    public Module getModule() {
        return module;
    }
}

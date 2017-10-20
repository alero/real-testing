package org.hrodberaht.injection.plugin.junit.cdi.config.ioc;

import org.hrodberaht.injection.Module;
import org.hrodberaht.injection.plugin.junit.plugins.DataSourcePlugin;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

import javax.sql.DataSource;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class ExampleModuleInternal {

    public static final String DATASOURCE = "MyDataSource";

    private Module module;

    public ExampleModuleInternal(InjectionRegistryBuilder configBase, DataSourcePlugin dataSourcePlugin) {
        DataSource dataSource = dataSourcePlugin.getCreator(DataSource.class).create(DATASOURCE);
        dataSourcePlugin.loadSchema(dataSource, "test");
        module = configBase
                .scan(() -> "org.hrodberaht.injection.plugin.junit.cdi.service2")
                .resource(builder -> builder.resource(DATASOURCE, dataSource))
                .getModule();
    }

    public Module getModule() {
        return module;
    }
}

package org.hrodberaht.injection.plugin.junit.config;

import org.hrodberaht.injection.plugin.junit.PluggableContainerConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.LiquibasePlugin;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

import javax.sql.DataSource;

public class ContainerConfigExample extends PluggableContainerConfigBase {

    @Override
    public void register(InjectionRegistryBuilder registryBuilder) {
        LiquibasePlugin liquibasePlugin = activatePlugin(LiquibasePlugin.class);
        DataSource dataSource = liquibasePlugin.getCreator(DataSource.class).create();

        liquibasePlugin.load(dataSource, "db.changelog-test.xml");

        registryBuilder.resource(builder ->
                builder.resource(DataSource.class, dataSource)
        );
    }
}

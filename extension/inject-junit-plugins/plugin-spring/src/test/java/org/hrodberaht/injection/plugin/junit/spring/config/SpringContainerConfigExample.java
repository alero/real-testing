package org.hrodberaht.injection.plugin.junit.spring.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.plugin.junit.PluggableContainerConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.DataSourcePlugin;
import org.hrodberaht.injection.plugin.junit.spring.plugins.SpringPlugin;
import org.hrodberaht.injection.spi.ResourceCreator;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

import javax.sql.DataSource;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:37:42
 * @version 2.0
 * @since 2.0
 */
public class SpringContainerConfigExample extends PluggableContainerConfigBase{

    static final String _package = "org.hrodberaht.injection.plugin.junit.spring.testservices.simple";

    @Override
    protected void register(InjectionRegistryBuilder registryBuilder) {
        String dataSourceName = "MyDataSource";
        DataSourcePlugin dataSourcePlugin = activatePlugin(DataSourcePlugin.class);
        DataSource dataSource = dataSourcePlugin.getCreator(DataSource.class).create(dataSourceName);

        dataSourcePlugin.loadSchema(dataSource, "sql");
        dataSourcePlugin.loadSchema(dataSource, "sql/insert");

        activatePlugin(SpringPlugin.class).loadConfig(
                "/META-INF/spring-config.xml"
        );

        registryBuilder.scan(() -> _package);

    }
}

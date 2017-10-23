package org.hrodberaht.injection.plugin.junit.spring.config;

import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.DataSourcePlugin;
import org.hrodberaht.injection.plugin.junit.plugins.SpringExtensionPlugin;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

import javax.sql.DataSource;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:37:42
 * @version 2.0
 * @since 2.0
 */
public class SpringContainerConfigExample extends ContainerContextConfigBase {

    static final String _package = "org.hrodberaht.injection.plugin.junit.spring.testservices.simple";

    @Override
    public void register(InjectionRegistryBuilder registryBuilder) {
        String dataSourceName = "MyDataSource2";
        DataSourcePlugin dataSourcePlugin = activatePlugin(DataSourcePlugin.class);
        DataSource dataSource = dataSourcePlugin.getCreator(DataSource.class).create(dataSourceName);

        dataSourcePlugin.loadSchema(dataSource, "sql");
        dataSourcePlugin.addBeforeTestSuite((loader) -> loader.get(LoadingTheTestWithData.class).run());

        activatePlugin(SpringExtensionPlugin.class).loadConfig(
                "/META-INF/spring-config.xml"
        );

        registryBuilder.scan(() -> _package);

    }
}

package org.hrodberaht.injection.plugin.junit.spring.config;

import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.DataSourcePlugin;
import org.hrodberaht.injection.plugin.junit.plugins.SpringExtensionPlugin;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

import javax.sql.DataSource;

import static org.hrodberaht.injection.plugin.junit.spring.config.SpringContainerConfigExample._package;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:37:42
 * @version 2.0
 * @since 2.0
 */
public class SpringContainerJavaConfigExample extends ContainerContextConfigBase {

    @Override
    public void register(InjectionRegistryBuilder registryBuilder) {
        String dataSourceName = "MyDataSource";
        DataSourcePlugin dataSourcePlugin = activatePlugin(DataSourcePlugin.class);
        DataSource dataSource = dataSourcePlugin.getCreator(DataSource.class).create(dataSourceName);

        dataSourcePlugin.loadSchema(dataSource, "sql");
        dataSourcePlugin.loadSchema(dataSource, "sql/insert");

        activatePlugin(SpringExtensionPlugin.class).loadConfig(SpringConfigJavaSample.class);

        registryBuilder.scan(() -> _package);
    }

}

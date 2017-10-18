package org.hrodberaht.injection.plugin.junit.spring.config;

import org.hrodberaht.injection.plugin.junit.PluggableContainerConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.DataSourcePlugin;
import org.hrodberaht.injection.plugin.junit.spring.beans.incubator.SpringBeanReplacementProxy;
import org.hrodberaht.injection.plugin.junit.spring.plugins.SpringExtensionPlugin;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

import javax.sql.DataSource;

import static org.hrodberaht.injection.plugin.junit.spring.config.SpringContainerConfigExample._package;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:37:42
 * @version 2.0
 * @since 2.0
 */
public class SpringContainerJavaConfigComboExample extends PluggableContainerConfigBase {


    @Override
    protected void register(InjectionRegistryBuilder registryBuilder) {
        String dataSourceName = "MyDataSource";
        DataSourcePlugin dataSourcePlugin = activatePlugin(DataSourcePlugin.class);
        DataSource dataSource = dataSourcePlugin.getCreator(DataSource.class).create(dataSourceName);

        dataSourcePlugin.loadSchema(dataSource, "sql");
        dataSourcePlugin.loadSchema(dataSource, "sql/insert");

        activatePlugin(SpringExtensionPlugin.class).loadConfig(
                SpringConfigJavaComboSample.class,
                SpringBeanReplacementProxy.class);

        registryBuilder.scan(() -> _package);
    }


/*    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("org.hrodberaht.injection.extensions.spring.testservices.simple");
    }
*/
}

package org.hrodberaht.injection.plugin.junit.cdi.config;

import org.hrodberaht.injection.plugin.junit.PluggableContainerConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.CDIInjectionPlugin;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:37:42
 * @version 1.0
 * @since 1.0
 */
public class CDIContainerConfigExample extends PluggableContainerConfigBase {

    public CDIContainerConfigExample() {
        activatePlugin(CDIInjectionPlugin.class);
    }

    @Override
    protected void register(InjectionRegistryBuilder injectionRegistryBuilder) {
        injectionRegistryBuilder.scan(() -> "org.hrodberaht.injection.plugin.junit.cdi.service");
    }

    /*
    public CDIContainerConfigExample() {

        String dataSourceName = "MyDataSource";
        DataSource dataSource = createDataSource(dataSourceName);
        // Named resource
        addResource(dataSourceName, dataSource);

    }

    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("test.org.hrodberaht.inject.extension.cdi.service");
    }
*/

}

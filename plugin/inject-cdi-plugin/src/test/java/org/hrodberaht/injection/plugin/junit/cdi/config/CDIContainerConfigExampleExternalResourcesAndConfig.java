package org.hrodberaht.injection.plugin.junit.cdi.config;

import org.hrodberaht.injection.plugin.junit.PluggableContainerConfigBase;
import org.hrodberaht.injection.plugin.junit.cdi.config.ioc.ExampleModuleExternal;
import org.hrodberaht.injection.plugin.junit.cdi.config.ioc.ExampleModuleInternal;
import org.hrodberaht.injection.plugin.junit.plugins.CDIInjectionPlugin;
import org.hrodberaht.injection.plugin.junit.plugins.DataSourcePlugin;
import org.hrodberaht.injection.plugin.junit.plugins.JpaPlugin;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:37:42
 * @version 1.0
 * @since 1.0
 */
public class CDIContainerConfigExampleExternalResourcesAndConfig extends PluggableContainerConfigBase {
    @Override
    protected void register(InjectionRegistryBuilder injectionRegistryBuilder) {
        activatePlugin(CDIInjectionPlugin.class);
        JpaPlugin jpaPlugin = activatePlugin(JpaPlugin.class);
        new ExampleModuleInternal(injectionRegistryBuilder, jpaPlugin);
        new ExampleModuleExternal(injectionRegistryBuilder, jpaPlugin);

    }

    /*private CDIApplication cdiApplication = null;

    public CDIContainerConfigExampleExternalResourcesAndConfig() {
        cdiApplication = new CDIApplication(this);
        cdiApplication.add(new ExampleModuleExternal(this).getModule());
        cdiApplication.add(new ExampleModuleInternal(this).getModule());

        this.addSQLSchemas(ExampleModuleInternal.DATASOURCE, "test");
    }

    @Override
    public InjectContainer createContainer() {
        // This will actually register the container and run the CDI extensions
        return cdiApplication.createContainer();
    }
    */


}

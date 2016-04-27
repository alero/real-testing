package test.org.hrodberaht.inject.extension.cdi.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.cdi.CDIApplication;
import test.org.hrodberaht.inject.extension.cdi.config.ioc.ExampleModuleExternal;
import test.org.hrodberaht.inject.extension.cdi.config.ioc.ExampleModuleInternal;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:37:42
 * @version 1.0
 * @since 1.0
 */
public class CDIContainerConfigExampleExternalResourcesAndConfig extends TDDCDIContainerConfigBase {

    private CDIApplication cdiApplication = null;

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


}

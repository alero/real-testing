package org.hrodberaht.injection.extensions.spring.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.ScopeContainer;
import org.hrodberaht.injection.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.extensions.spring.SpringContainerConfigBase;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.spi.ResourceCreator;

import javax.sql.DataSource;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:37:42
 * @version 1.0
 * @since 1.0
 */
public class SpringContainerConfigExample extends SpringContainerConfigBase {

    public SpringContainerConfigExample() {

        // Adding the spring config, will automate the resource registration to also be done to the spring config
        addSpringConfig("/META-INF/spring-config.xml");

        String dataSourceName = "MyDataSource";
        DataSource dataSource = createDataSource(dataSourceName);
        // Named resource
        addResource(dataSourceName, dataSource);


    }



    @Override
    protected ResourceCreator createResourceCreator() {
        return null;
    }

    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("org.hrodberaht.inject.extension.spring.testservices.simple");
    }

    @Override
    protected void injectResources(Object serviceInstance) {

    }

    @Override
    protected InjectionRegisterScanBase getScanner(InjectionRegister injectionRegister) {
        return new InjectionRegisterScanBase(injectionRegister){
            @Override
            public boolean isInterfaceAnnotated(Class aClazz) {
                return false;
            }

            @Override
            public boolean isServiceAnnotated(Class aClazz) {
                return false;
            }

            @Override
            public ScopeContainer.Scope getScope(Class serviceClass) {
                return null;
            }

            @Override
            public InjectionRegisterScanBase clone() {
                return null;
            }
        };
    }


}

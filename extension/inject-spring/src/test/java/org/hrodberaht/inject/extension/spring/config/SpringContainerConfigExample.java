package org.hrodberaht.inject.extension.spring.config;

import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.ScopeContainer;
import org.hrodberaht.inject.config.InjectionRegisterScanBase;
import org.hrodberaht.inject.extension.tdd.internal.TDDContainerConfigBase;
import org.hrodberaht.inject.register.InjectionRegister;
import org.hrodberaht.inject.spi.InjectionRegisterScanInterface;

import javax.sql.DataSource;

/**
 * Unit Test EJB (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:37:42
 * @version 1.0
 * @since 1.0
 */
public class SpringContainerConfigExample extends TDDContainerConfigBase {

    public SpringContainerConfigExample() {

        String dataSourceName = "MyDataSource";
        DataSource dataSource = createDataSource(dataSourceName);
        // Named resource
        addResource(dataSourceName, dataSource);

    }

    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("org.hrodberaht.inject.extension.spring.testservices.simple");
    }

    @Override
    protected void injectResources(Object serviceInstance) {

    }

    @Override
    protected InjectionRegisterScanInterface getScanner(InjectionRegister injectionRegister) {
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

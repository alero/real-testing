package test.config;

import com.JUnitStreamCDIContainerConfig;
import org.hrodberaht.injection.extensions.cdi.stream.CDIInjectionRegistryStream;
import org.hrodberaht.injection.internal.ScopeContainer;
import test.service.CDIServiceInterface;
import test.service.CDIServiceInterfaceImpl;
import test.service.SimpleService;
import test.service.SimpleServiceSingleton;
import test.service.SimpleServiceSingletonAnnotated;

import javax.sql.DataSource;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:37:42
 * @version 1.0
 * @since 1.0
 */
public class CDIContainerConfigExample extends JUnitStreamCDIContainerConfig {

    public CDIContainerConfigExample() {

        String dataSourceName = "MyDataSource";
        DataSource dataSource = createDataSource(dataSourceName);
        // Named resource
        addResource(dataSourceName, dataSource);

    }

    @Override
    protected void registerStream(CDIInjectionRegistryStream stream) {
        stream
                .register(registrations -> {
                    registrations.register(CDIServiceInterface.class).with(CDIServiceInterfaceImpl.class);
                    registrations.register(SimpleServiceSingleton.class).scopeAs(ScopeContainer.Scope.SINGLETON);
                    registrations.register(SimpleServiceSingletonAnnotated.class);
                    registrations.register(SimpleService.class);
                });
    }


}
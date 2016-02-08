package test.org.hrodberaht.inject.extension.ejbunit.ejb3.config;

import org.hrodberaht.inject.register.RegistrationModuleAnnotation;
import org.mockito.Mockito;
import test.org.hrodberaht.inject.extension.ejbunit.ejb3.service.EJB3InnerServiceInterface;

/**
 * Unit Test EJB (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-17 02:44:35
 * @version 1.0
 * @since 1.0
 */
public class MockedInnerModule extends RegistrationModuleAnnotation {
    @Override
    public void registrations() {
        EJB3InnerServiceInterface innerServiceInterface = Mockito.mock(EJB3InnerServiceInterface.class);
        Mockito.when(innerServiceInterface.findSomething(12L)).thenReturn("Mocked");
        register(EJB3InnerServiceInterface.class).withInstance(innerServiceInterface);

    }
}

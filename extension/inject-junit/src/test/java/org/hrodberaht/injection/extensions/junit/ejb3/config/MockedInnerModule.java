package org.hrodberaht.injection.extensions.junit.ejb3.config;

import org.hrodberaht.injection.extensions.junit.ejb3.service.EJB3InnerServiceInterface;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.mockito.Mockito;

/**
 * Unit Test JUnit (using @Inject)
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

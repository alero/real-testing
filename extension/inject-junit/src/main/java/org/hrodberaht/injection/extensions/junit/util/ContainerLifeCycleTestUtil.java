package org.hrodberaht.injection.extensions.junit.util;

import org.hrodberaht.injection.internal.InjectionContainerManager;
import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.register.RegistrationModule;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;

import javax.inject.Inject;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-13 00:23:43
 * @version 1.0
 * @since 1.0
 * <p/>
 */
public class ContainerLifeCycleTestUtil {

    @Inject
    private InjectionRegisterModule module;


    public void registerServiceInstance(Class serviceDefinition, Object service) {
        RegistrationModuleAnnotation registrationModule = new RegistrationModuleAnnotation(){
            @Override
            public void registrations() {
                register(serviceDefinition)
                        .registerTypeAs(InjectionContainerManager.RegisterType.OVERRIDE_NORMAL)
                        .withInstance(service);
            }
        };
        module.register(registrationModule);
    }

    public void registerModule(RegistrationModule module) {
        this.module.register(module);
    }


    public <T> T getService(Class<T> aClass) {
        return module.getContainer().get(aClass);
    }
}

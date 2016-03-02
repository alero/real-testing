package org.hrodberaht.inject.extension.tdd;

import org.hrodberaht.inject.register.RegistrationModule;
import org.hrodberaht.inject.spi.ContainerConfig;

import javax.inject.Inject;

/**
 * Unit Test EJB (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-13 00:23:43
 * @version 1.0
 * @since 1.0
 * <p/>
 * TODO: replace the ThreadConfigHolder with a stateaware injection supporting multiple containers in the same VM and no more need for thread initializers
 */
public class ContainerLifeCycleTestUtil {

    @Inject
    private ContainerConfig containerConfig;


    public void registerServiceInstance(Class serviceDefinition, Object service) {
        containerConfig.getActiveRegister().overrideRegister(serviceDefinition, service);
    }

    public void registerModule(RegistrationModule module) {
        containerConfig.getActiveRegister().register(module);
    }


    public <T> T getService(Class<T> aClass) {
        return containerConfig.getActiveContainer().get(aClass);
    }
}

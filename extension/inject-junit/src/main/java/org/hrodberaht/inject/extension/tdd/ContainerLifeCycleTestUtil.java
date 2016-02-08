package org.hrodberaht.inject.extension.tdd;

import org.hrodberaht.inject.internal.InjectionContainer;
import org.hrodberaht.inject.register.RegistrationModule;
import org.hrodberaht.inject.spi.ContainerConfig;
import org.hrodberaht.inject.spi.ThreadConfigHolder;

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
    private InjectionContainer injectionContainer;

    protected static void begin(ContainerConfig theContainer) {
        ThreadConfigHolder.set(theContainer);
    }

    protected static void end() {
        ThreadConfigHolder.get().cleanActiveContainer();
        ThreadConfigHolder.remove();
    }

    public static void registerServiceInstance(Class serviceDefinition, Object service) {
        ThreadConfigHolder.get().getActiveRegister().overrideRegister(serviceDefinition, service);
    }

    public static void registerModule(RegistrationModule module) {
        ThreadConfigHolder.get().getActiveRegister().register(module);
    }


    public static <T> T getService(Class<T> aClass) {
        ContainerConfig containerConfigBase = ThreadConfigHolder.get();
        return containerConfigBase.getActiveContainer().get(aClass);
    }
}

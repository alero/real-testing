package org.hrodberaht.inject.register;

import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.internal.annotation.InjectionFinder;
import org.hrodberaht.inject.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.inject.register.internal.RegistrationExtended;
import org.hrodberaht.inject.register.internal.RegistrationInstanceSimple;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jun-17 19:11:31
 * @version 1.0
 * @since 1.0
 */
public abstract class RegistrationModuleAnnotation implements RegistrationModule {

    private InjectionFinder injectionFinderImplementation;
    private InstanceCreator instanceCreatorImplementation;
    protected Map<RegistrationInstanceSimple, RegistrationInstanceSimple>
            registrations = new HashMap<RegistrationInstanceSimple, RegistrationInstanceSimple>();

    protected RegistrationModuleAnnotation() {
        registrations();
    }

    protected void registerInjectionFinder(InjectionFinder injectionFinder) {
        this.injectionFinderImplementation = injectionFinder;
    }

    protected void registerInstanceCreator(InstanceCreator instanceCreator) {
        this.instanceCreatorImplementation = instanceCreator;
    }

    public RegistrationExtended register(Class anyThing) {
        RegistrationInstanceSimple instance = new RegistrationInstanceSimple(anyThing);
        registrations.put(instance, instance);
        return instance;
    }

    public InjectionFinder getInjectionFinder() {
        return injectionFinderImplementation;
    }

    public InstanceCreator getInstanceCreator() {
        return instanceCreatorImplementation;
    }

    public Collection<RegistrationInstanceSimple> getRegistrations() {
        return registrations.values();
    }

    public abstract void registrations();

    public void postRegistration(InjectContainer injectContainer) {
        // Not a forced method
    }

    public void preRegistration(InjectContainer injectContainer) {
        // Not a forced method
    }
}

package org.hrodberaht.injection.register;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.injection.register.internal.RegistrationExtended;
import org.hrodberaht.injection.register.internal.RegistrationInstanceSimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * 2010-jun-17 19:11:31
 * @version 1.0
 * @since 1.0
 */
public abstract class RegistrationModuleAnnotation implements RegistrationModule<RegistrationInstanceSimple> {

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

    public RegistrationExtended register(Class serviceClass) {
        RegistrationInstanceSimple instance = new RegistrationInstanceSimple(serviceClass);
        registrations.put(instance, instance);
        return instance;
    }

    public RegistrationExtended register(Class serviceClass, Class interfaceClass) {
        RegistrationInstanceSimple instance = new RegistrationInstanceSimple(interfaceClass).with(serviceClass);
        registrations.put(instance, instance);
        return instance;
    }

    public InjectionFinder getInjectionFinder() {
        return injectionFinderImplementation;
    }

    public InstanceCreator getInstanceCreator() {
        return instanceCreatorImplementation;
    }

    @Override
    public Collection<RegistrationInstanceSimple> getRegistrations() {
        return registrations.values();
    }

    @Override
    public List<RegistrationInstanceSimple> getRegistrationsList() {
        return new ArrayList<>(registrations.values());
    }

    public void putRegistrations(List<RegistrationInstanceSimple> registrationList) {
        for (RegistrationInstanceSimple registrationInstanceSimple : registrationList) {
            registrations.put(registrationInstanceSimple, registrationInstanceSimple);
        }
    }

    public abstract void registrations();

    @Override
    public void postRegistration(InjectContainer injectContainer) {
        // Not a forced method
    }

    @Override
    public void preRegistration(InjectContainer injectContainer) {
        // Not a forced method
    }
}

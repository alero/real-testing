package org.hrodberaht.injection.spi.module;

import org.hrodberaht.injection.Module;
import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;

/**
 * Created by alexbrob on 2016-02-25.
 */
public class CustomInjectionPointFinderModule extends RegistrationModuleAnnotation {

    private final InjectionFinder injectionFinder;

    public CustomInjectionPointFinderModule(InjectionFinder injectionFinder) {
        this.injectionFinder = injectionFinder;
    }

    @Override
    public void registrations() {

    }

    @Override
    public InjectionFinder getInjectionFinder() {
        return injectionFinder;
    }
}

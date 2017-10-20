package org.hrodberaht.injection;

import org.hrodberaht.injection.register.RegistrationModuleAnnotationScanner;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class Module extends RegistrationModuleAnnotationScanner {

    private InjectContainer injectionContainer;

    public Module() {
    }

    public Module(InjectContainer injectionContainer) {
        this.injectionContainer = injectionContainer;
    }

    @Override
    public void scan() {
    }

    public InjectContainer getInjectionContainer() {
        return injectionContainer;
    }
}

package org.hrodberaht.injection;

import org.hrodberaht.injection.config.InjectionStoreFactory;
import org.hrodberaht.injection.register.InjectionRegister;

/**
 * Created by alexbrob on 2016-03-29.
 */
public class InjectionRegistryPlain implements InjectionRegistry {

    private InjectContainer injectionContainer;
    private InjectionRegister injectionRegister = InjectionStoreFactory.getInjectionRegister();

    public InjectionRegistryPlain register(Module module) {
        injectionRegister.register(module);
        injectionContainer = injectionRegister.getContainer();
        return this;
    }

    public InjectContainer getContainer() {
        return injectionContainer;
    }

    public Module getModule() {
        Module module = new Module(injectionContainer);
        injectionRegister.fillModule(module);
        return module;
    }
}

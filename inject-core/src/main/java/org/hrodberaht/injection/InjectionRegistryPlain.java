package org.hrodberaht.injection;

import org.hrodberaht.injection.internal.InjectionRegisterModule;

/**
 * Created by alexbrob on 2016-03-29.
 */
public class InjectionRegistryPlain implements InjectionRegistry {

    private InjectContainer injectionContainer;
    private InjectionRegisterModule injectionRegisterModule = new InjectionRegisterModule();

    public InjectionRegistryPlain register(Module module) {
        injectionRegisterModule.register(module);
        injectionContainer = injectionRegisterModule.getContainer();
        return this;
    }

    public InjectContainer getContainer() {
        return injectionContainer;
    }

    public Module getModule() {
        Module module = new Module(injectionContainer);
        injectionRegisterModule.fillModule(module);
        return module;
    }
}

package org.hrodberaht.injection.config;

import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.register.InjectionRegister;

public class InjectionStoreFactory {


    public static InjectionRegister getInjectionRegister() {
        return new InjectionRegisterModule();
    }

    public static InjectionRegister getInjectionRegister(InjectionRegister injectionRegister) {
        return new InjectionRegisterModule(injectionRegister);
    }

}

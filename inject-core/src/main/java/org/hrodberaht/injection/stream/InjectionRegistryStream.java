package org.hrodberaht.injection.stream;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.Module;
import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.register.internal.RegistrationInstanceSimple;

import java.util.List;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class InjectionRegistryStream {

    private InjectContainer injectionContainer;
    private InjectionRegisterModule injectionRegisterModule = new InjectionRegisterModule();

    public InjectContainer getContainer() {
        return injectionContainer;
    }

    public InjectionRegistryStream scan(ScanModuleFunc scanModuleFunc){
        String _packages = scanModuleFunc.scan();
        Module module = new Module() {
            @Override
            public void scan() {
                this.scanAndRegister(_packages);
            }
        };
        injectionRegisterModule.register(module);
        injectionContainer = injectionRegisterModule.getContainer();
        return this;
    }

    public InjectionRegistryStream register(RegisterModuleFunc scanModuleFunc){
        Registrations registrations = new Registrations();
        scanModuleFunc.register(registrations);
        List<RegistrationInstanceSimple> register = registrations.registry();
        Module module = new Module() {
            @Override
            public void registrations() {
                this.putRegistrations(register);
            }
        };
        injectionRegisterModule.register(module);
        injectionContainer = injectionRegisterModule.getContainer();
        return this;
    }

    public Module getModule() {
        Module module = new Module(injectionContainer);
        injectionRegisterModule.fillModule(module);
        return module;
    }
}

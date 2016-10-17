package org.hrodberaht.injection.internal;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.Module;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.RegistrationModule;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-aug-01 16:35:23
 * @version 1.0
 * @since 1.0
 */
public class InjectionRegisterModule extends InjectionRegisterBase<InjectionRegister> {

    private Collection<RegistrationModule> registeredModules = new ArrayList<RegistrationModule>();

    public InjectionRegisterModule() {
    }

    public InjectionRegisterModule(InjectionRegister register) {
        super(register);
    }

    public InjectionRegisterModule(Module module) {
        registeredModules.add(module);
        container.register(module);
    }

    public InjectionRegisterModule register(RegistrationModule... modules) {
        for (RegistrationModule module : modules) {
            registeredModules.add(module);
        }
        container.register(modules);
        return this;
    }

    public void printRegistration(PrintStream writer) {

        writer.println("--------- InjectionRegisterModule Information Printer --------------");
        writer.println("The following modules has been appended in order");
        for (RegistrationModule module : registeredModules) {
            writer.println("Module: " + module.getClass().getName());
        }

        super.printRegistration(writer);

    }

    @Override
    public InjectionRegisterModule clone() {
        InjectionRegisterModule registerModule = new InjectionRegisterModule();
        registerModule.registeredModules.addAll(this.registeredModules);
        try {
            registerModule.container = this.container.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return registerModule;
    }

    public void fillModule(Module module) {
        for(RegistrationModule registrationModule:registeredModules) {
            module.putRegistrations(registrationModule.getRegistrationsList());
        }
    }
}

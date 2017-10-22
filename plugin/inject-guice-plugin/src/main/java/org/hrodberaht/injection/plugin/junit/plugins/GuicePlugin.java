package org.hrodberaht.injection.plugin.junit.plugins;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.hrodberaht.injection.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.injection.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.plugin.junit.spi.Plugin;
import org.hrodberaht.injection.plugin.junit.spi.annotation.InjectionPluginInjectionFinder;
import org.hrodberaht.injection.plugin.junit.spi.annotation.InjectionPluginInjectionRegister;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.spi.ContainerConfigBuilder;

import java.util.List;

public class GuicePlugin implements Plugin {

    private Injector injector;

    @InjectionPluginInjectionRegister
    private void setInjectionRegister(InjectionRegister injectionRegister) {
        if (injector != null) {
            injectionRegister.register(new RegistrationModuleAnnotation() {
                @Override
                public void registrations() {
                    register(Injector.class).withFactoryInstance(injector);
                }
            });
        }
    }

    @InjectionPluginInjectionFinder
    private InjectionFinder getInjectionFinder(ContainerConfigBuilder containerConfigBuilder) {
        return new DefaultInjectionPointFinder(containerConfigBuilder) {
            @Override
            public Object extendedInjection(Object instance) {
                // This will rewire the instance to become a "guice-instance"
                instance = injector.getInstance(instance.getClass());
                return super.extendedInjection(instance);
            }
        };
    }

    @Override
    public LifeCycle getLifeCycle() {
        return LifeCycle.TEST_SUITE;
    }


    public GuicePlugin loadModules(Module... modules) {
        injector = Guice.createInjector(modules);
        return this;
    }

    public GuicePlugin loadModules(List<Module> modules) {
        injector = Guice.createInjector(modules);
        return this;
    }
}

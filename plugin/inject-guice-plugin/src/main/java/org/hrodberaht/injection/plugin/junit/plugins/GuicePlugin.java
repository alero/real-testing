package org.hrodberaht.injection.plugin.junit.plugins;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.hrodberaht.injection.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.injection.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.plugin.junit.resources.ChainableInjectionPointProvider;
import org.hrodberaht.injection.plugin.junit.spi.InjectionPlugin;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.spi.ContainerConfigBuilder;

import java.util.List;

public class GuicePlugin implements InjectionPlugin {

    private Injector injector;

    @Override
    public void setInjectionRegister(InjectionRegister injectionRegister) {
        if(injector != null) {
            injectionRegister.register(new RegistrationModuleAnnotation() {
                @Override
                public void registrations() {
                    register(Injector.class).withFactoryInstance(injector);
                }
            });
        }
    }

    @Override
    public InjectionFinder getInjectionFinder(ContainerConfigBuilder containerConfigBuilder) {

        return new ChainableInjectionPointProvider(
                new DefaultInjectionPointFinder(containerConfigBuilder)
        ){
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
        return LifeCycle.TESTSUITE;
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

package org.hrodberaht.injection.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.Module;
import org.hrodberaht.injection.internal.*;
import org.hrodberaht.injection.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.injection.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.spi.ContainerConfigBuilder;
import org.hrodberaht.injection.spi.ResourceCreator;
import org.hrodberaht.injection.spi.ResourceFactory;
import org.hrodberaht.injection.spi.module.CustomInjectionPointFinderModule;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:35:27
 * @version 1.0
 * @since 1.0
 */
public abstract class ContainerConfig<T extends InjectionRegister> implements ContainerConfigBuilder {

    protected InjectionRegister originalRegister = null;
    protected InjectionRegister activeRegister = null;

    protected ResourceFactory resourceFactory = createResourceFactory();

    protected ResourceInject resourceInjection = createResourceInject();

    protected abstract ResourceFactory createResourceFactory();

    protected abstract ResourceInject createResourceInject();

    public abstract void injectResources(Object serviceInstance);

    protected abstract void appendResources(InjectionRegister registerModule);

    protected void registerModules(InjectionRegister activeRegister) {
        // This is intended for loading modules
    }

    public void start(){
        InjectionRegistryBuilder combinedRegister = new InjectionRegistryBuilder(this);
        register(combinedRegister);
        combinedRegister.register(
                registrations -> registrations.register(
                        new CustomInjectionPointFinderModule(createDefaultInjectionPointFinder())
                )
        );
        originalRegister = combinedRegister.build();
        appendResources(originalRegister);
        activeRegister = originalRegister.copy();
    }

    public abstract void register(InjectionRegistryBuilder registryBuilder);

    public T getActiveRegister() {
        return (T) activeRegister;
    }

    public void addSingletonActiveRegistry() {
        RegistrationModuleAnnotation injectionRegisterModuleConfig = prepareModuleSingletonForRegistry();
        activeRegister.register(injectionRegisterModuleConfig);
    }

    private RegistrationModuleAnnotation prepareModuleSingletonForRegistry() {
        final InjectionRegister configBase = this.activeRegister;
        return new RegistrationModuleAnnotation() {
            @Override
            public void registrations() {
                register(InjectionRegister.class)
                        // .named("ActiveRegisterModule")
                        .scopeAs(ScopeContainer.Scope.SINGLETON)
                        .registerTypeAs(InjectionContainerManager.RegisterType.FINAL)
                        .withInstance(configBase);
            }
        };
    }

    public void cleanActiveContainer() {
        activeRegister = originalRegister.copy();
    }

    public ResourceFactory getResourceFactory() {
        return resourceFactory;
    }

    protected InjectionFinder createDefaultInjectionPointFinder() {
        return new DefaultInjectionPointFinder(this);
    }
}

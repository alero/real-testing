package org.hrodberaht.injection.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.Module;
import org.hrodberaht.injection.internal.InjectionContainerManager;
import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.internal.ResourceInjection;
import org.hrodberaht.injection.internal.ScopeContainer;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.spi.ContainerConfig;
import org.hrodberaht.injection.spi.ResourceCreator;

import javax.sql.DataSource;
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
public abstract class ContainerConfigBase<T extends InjectionRegisterModule> implements ContainerConfig {

    protected InjectionRegisterModule originalRegister = null;
    protected InjectionRegisterModule activeRegister = null;

    protected ResourceCreator resourceCreator = createResourceCreator();

    protected ResourceInjection resourceInjection = createResourceInjector();

    protected abstract ResourceCreator createResourceCreator();

    protected abstract ResourceInjection createResourceInjector();

    public abstract InjectContainer createContainer();

    protected abstract void injectResources(Object serviceInstance);

    protected void registerModules(InjectionRegisterModule combinedRegister) {
        // This is intended for loading modules
    }

    protected abstract InjectionRegisterScanBase getScanner(InjectionRegister registerModule);

    protected InjectContainer createAutoScanContainer(String... packageName) {
        InjectionRegisterModule combinedRegister = preScanModuleRegistration();
        registerModules(combinedRegister);
        createAutoScanContainerRegister(packageName, combinedRegister);
        return activeRegister.getContainer();
    }

    protected InjectContainer createEmptyContainer() {
        InjectionRegisterModule combinedRegister = preScanModuleRegistration();
        createAutoScanContainerRegister(null, combinedRegister);
        return activeRegister.getContainer();
    }

    protected void createAutoScanContainerRegister(String[] packageName, InjectionRegisterModule combinedRegister) {
        if (packageName != null) {
            scanAndRegister(combinedRegister, packageName);
        }
        originalRegister = combinedRegister;
        appendTypedResources(originalRegister);
        activeRegister = originalRegister.clone();
    }

    private void scanAndRegister(InjectionRegisterModule combinedRegister, String[] packageName) {
        InjectionRegisterScanBase registerScan = getScanner(combinedRegister);
        registerScan.scanPackage(packageName);
    }

    protected InjectionRegisterModule preScanModuleRegistration() {
        return new InjectionRegisterModule();
    }

    public void addSingletonActiveRegistry() {
        RegistrationModuleAnnotation injectionRegisterModuleConfig = prepareModuleSingletonForRegistry();
        activeRegister.register(injectionRegisterModuleConfig);
    }

    private RegistrationModuleAnnotation prepareModuleSingletonForRegistry() {
        final InjectionRegisterModule configBase = this.activeRegister;
        return new RegistrationModuleAnnotation() {
            @Override
            public void registrations() {
                register(InjectionRegisterModule.class)
                        // .named("ActiveRegisterModule")
                        .scopeAs(ScopeContainer.Scope.SINGLETON)
                        .registerTypeAs(InjectionContainerManager.RegisterType.FINAL)
                        .withInstance(configBase);
            }
        };
    }

    protected void appendTypedResources(InjectionRegisterModule registerModule) {
        Map<Class, Object> typedResources = resourceInjection.getTypeResources();
        if (typedResources != null) {
            for (final Class typedResource : typedResources.keySet()) {
                final Object value = typedResources.get(typedResource);
                registerModule.register(new RegistrationModuleAnnotation() {
                    @Override
                    public void registrations() {
                        register(typedResource).withInstance(value);
                    }
                });
            }
        }
    }

    public T getActiveRegister() {
        return (T) activeRegister;
    }

    public InjectContainer getActiveContainer() {
        return activeRegister.getContainer();
    }

    public void cleanActiveContainer() {
        activeRegister = originalRegister.clone();
    }

    public void addResource(String name, Object value) {
        resourceInjection.addResource(name, value);
    }

    public void addResource(Class typedName, Object value) {
        resourceInjection.addResource(typedName, value);
    }

    public DataSource createDataSource(String dataSourceName) {
        return resourceCreator.createDataSource(dataSourceName);
    }

    public boolean hasDataSource(String dataSourceName) {
        return resourceCreator.hasDataSource(dataSourceName);
    }

    public DataSource getDataSource(String dataSourceName) {
        return resourceCreator.getDataSource(dataSourceName);
    }

    /**
     * Loads all the modules and creates the originalRegsiter and the active register,
     * this is actually only used in the JUnit version of the container and could be redesigned
     *
     * @param modules
     * @return the active InjectionContainer with all services loaded and registered
     */
    public InjectContainer loadModule(List<Module> modules) {
        InjectionRegisterModule injectionRegisterModule = new InjectionRegisterModule();
        for (Module module : modules) {
            injectionRegisterModule.register(module);
        }
        originalRegister = injectionRegisterModule;
        appendTypedResources(originalRegister);
        activeRegister = originalRegister.clone();
        return activeRegister.getContainer();
    }


}

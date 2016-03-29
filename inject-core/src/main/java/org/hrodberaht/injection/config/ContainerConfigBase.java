package org.hrodberaht.injection.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.InjectionContainerManager;
import org.hrodberaht.injection.InjectionRegisterModule;
import org.hrodberaht.injection.ScopeContainer;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.spi.ContainerConfig;
import org.hrodberaht.injection.spi.ResourceCreator;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.HashMap;
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

    protected Map<String, Object> resources = null;
    protected Map<Class, Object> typedResources = null;

    protected ResourceCreator resourceCreator = createResourceCreator();

    protected abstract ResourceCreator createResourceCreator();

    public abstract InjectContainer createContainer();

    protected abstract void injectResources(Object serviceInstance);

    protected abstract InjectionRegisterScanBase getScanner(InjectionRegister registerModule);

    protected InjectContainer createAutoScanContainer(String... packageName) {
        InjectionRegisterModule combinedRegister = preScanModuleRegistration();
        createAutoScanContainerRegister(packageName, combinedRegister);
        return activeRegister.getContainer();
    }

    protected void createAutoScanContainerRegister(String[] packageName, InjectionRegisterModule combinedRegister) {

        scanAndRegister(combinedRegister, packageName);

        originalRegister = combinedRegister;
        appendTypedResources();
        activeRegister = originalRegister.clone();
        System.out.println("originalRegister - " + originalRegister);
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
        return new RegistrationModuleAnnotation(){
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

    protected void appendTypedResources() {
        if (typedResources != null) {
            for (final Class typedResource : typedResources.keySet()) {
                final Object value = typedResources.get(typedResource);
                originalRegister.register(new RegistrationModuleAnnotation() {
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

    protected void addResource(String name, Object value) {
        if (resources == null) {
            resources = new HashMap<String, Object>();
        }
        resources.put(name, value);
    }

    protected void addResource(Class typedName, Object value) {
        if (typedResources == null) {
            typedResources = new HashMap<Class, Object>();
        }
        typedResources.put(typedName, value);
    }

    protected DataSource createDataSource(String dataSourceName) {
        return resourceCreator.createDataSource(dataSourceName);
    }

    protected boolean hasDataSource(String dataSourceName) {
        return resourceCreator.hasDataSource(dataSourceName);
    }

    protected boolean injectTypedResource(Object serviceInstance, Field field) {
        if (typedResources == null) {
            return false;
        }
        Object value = typedResources.get(field.getType());
        if (value != null) {
            injectResourceValue(serviceInstance, field, value);
            return true;
        }
        return false;
    }

    protected boolean injectNamedResource(Object serviceInstance, Field field, Resource resource) {
        if (resources == null) {
            return false;
        }
        Object value = resources.get(resource.name());
        if (value == null) {
            value = resources.get(resource.mappedName());
        }
        if (value != null) {
            injectResourceValue(serviceInstance, field, value);
            return true;
        }
        return false;
    }

    protected void injectResourceValue(Object serviceInstance, Field field, Object value) {
        if (value != null) {
            boolean accessible = field.isAccessible();
            try {
                if (!accessible) {
                    field.setAccessible(true);
                }
                field.set(serviceInstance, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                if (!accessible) {
                    field.setAccessible(false);
                }
            }
        }
    }
}

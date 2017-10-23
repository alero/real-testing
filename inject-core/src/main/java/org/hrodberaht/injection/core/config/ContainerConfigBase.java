/*
 * Copyright (c) 2017 org.hrodberaht
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hrodberaht.injection.core.config;

import org.hrodberaht.injection.core.InjectContainer;
import org.hrodberaht.injection.core.Module;
import org.hrodberaht.injection.core.internal.InjectionContainerManager;
import org.hrodberaht.injection.core.internal.InjectionRegisterModule;
import org.hrodberaht.injection.core.internal.ResourceInjection;
import org.hrodberaht.injection.core.internal.ScopeContainer;
import org.hrodberaht.injection.core.register.InjectionRegister;
import org.hrodberaht.injection.core.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.core.spi.ContainerConfig;
import org.hrodberaht.injection.core.spi.ResourceCreator;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:35:27
 * @version 1.0
 * @since 1.0
 */
public abstract class ContainerConfigBase<T extends InjectionRegister> implements ContainerConfig {

    protected InjectionRegister originalRegister = null;
    protected InjectionRegister activeRegister = null;

    protected ResourceCreator resourceCreator = createResourceCreator();

    protected ResourceInjection resourceInjection = createResourceInjector();

    protected abstract ResourceCreator createResourceCreator();

    protected abstract ResourceInjection createResourceInjector();

    public abstract InjectContainer createContainer();

    protected abstract void injectResources(Object serviceInstance);

    protected void registerModules(InjectionRegister activeRegister) {
        // This is intended for loading modules
    }

    public T getActiveRegister() {
        return (T) activeRegister;
    }


    protected abstract InjectionRegisterScanBase getScanner(InjectionRegister registerModule);

    protected InjectContainer createAutoScanContainer(String... packageName) {
        InjectionRegister combinedRegister = preScanModuleRegistration();
        registerModules(combinedRegister);
        createAutoScanContainerRegister(packageName, combinedRegister);
        return activeRegister.getContainer();
    }

    protected InjectContainer createContainer(Module module) {
        InjectionRegister combinedRegister = preScanModuleRegistration(module);
        registerModules(combinedRegister);
        createAutoScanContainerRegister(null, combinedRegister);
        return activeRegister.getContainer();
    }

    protected InjectionRegister preScanModuleRegistration(Module module) {
        return new InjectionRegisterModule(module);
    }

    protected InjectContainer createEmptyContainer() {
        InjectionRegister combinedRegister = preScanModuleRegistration();
        registerModules(combinedRegister);
        createAutoScanContainerRegister(null, combinedRegister);
        return activeRegister.getContainer();
    }

    protected void createAutoScanContainerRegister(String[] packageName, InjectionRegister combinedRegister) {
        registerModules(combinedRegister);
        if (packageName != null) {
            scanAndRegister(combinedRegister, packageName);
        }
        originalRegister = combinedRegister;
        appendTypedResources(originalRegister);
        activeRegister = originalRegister.copy();
    }

    private void scanAndRegister(InjectionRegister combinedRegister, String[] packageName) {
        InjectionRegisterScanBase registerScan = getScanner(combinedRegister);
        registerScan.scanPackage(packageName);
    }

    protected InjectionRegister preScanModuleRegistration() {
        return InjectionStoreFactory.getInjectionRegister();
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

    protected void appendTypedResources(InjectionRegister registerModule) {
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


    public InjectContainer getActiveContainer() {
        return activeRegister.getContainer();
    }

    public void cleanActiveContainer() {
        activeRegister = originalRegister.copy();
    }

    public void addResource(String name, Object value) {
        resourceInjection.addResource(name, value);
    }

    public void addResource(Class typedName, Object value) {
        resourceInjection.addResource(typedName, value);
    }

    public DataSource createDataSource(String dataSourceName, String databaseName) {
        return resourceCreator.createDataSource(databaseName, dataSourceName);
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
        InjectionRegister injectionRegisterModule = InjectionStoreFactory.getInjectionRegister();
        for (Module module : modules) {
            injectionRegisterModule.register(module);
        }
        originalRegister = injectionRegisterModule;
        appendTypedResources(originalRegister);
        activeRegister = originalRegister.copy();
        return activeRegister.getContainer();
    }


}

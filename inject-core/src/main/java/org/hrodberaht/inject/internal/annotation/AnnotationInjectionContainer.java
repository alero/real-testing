/*
 * ~ Copyright (c) 2010.
 *   ~ Licensed under the Apache License, Version 2.0 (the "License");
 *   ~ you may not use this file except in compliance with the License.
 *   ~ You may obtain a copy of the License at
 *   ~
 *   ~        http://www.apache.org/licenses/LICENSE-2.0
 *   ~
 *   ~ Unless required by applicable law or agreed to in writing, software
 *   ~ distributed under the License is distributed on an "AS IS" BASIS,
 *   ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   ~ See the License for the specific language governing permissions and limitations under the License.
 */

package org.hrodberaht.inject.internal.annotation;

import org.hrodberaht.inject.ScopeContainer;
import org.hrodberaht.inject.SimpleInjection;
import org.hrodberaht.inject.internal.InjectionContainer;
import org.hrodberaht.inject.internal.InjectionContainerBase;
import org.hrodberaht.inject.internal.InjectionKey;
import org.hrodberaht.inject.internal.RegistrationInjectionContainer;
import org.hrodberaht.inject.internal.ServiceRegister;
import org.hrodberaht.inject.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.inject.internal.annotation.scope.FactoryScopeHandler;
import org.hrodberaht.inject.internal.annotation.scope.SingletonScopeHandler;
import org.hrodberaht.inject.internal.annotation.scope.VariableFactoryScopeHandler;
import org.hrodberaht.inject.internal.exception.DuplicateRegistrationException;
import org.hrodberaht.inject.internal.exception.InjectRuntimeException;
import org.hrodberaht.inject.register.RegistrationModule;
import org.hrodberaht.inject.register.RegistrationModuleAnnotation;
import org.hrodberaht.inject.register.RegistrationModuleAnnotationScanner;
import org.hrodberaht.inject.register.VariableInjectionFactory;
import org.hrodberaht.inject.register.internal.RegistrationInstanceSimple;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-29 12:38:22
 * @version 1.0
 * @since 1.0
 */
public class AnnotationInjectionContainer extends InjectionContainerBase
        implements InjectionContainer, RegistrationInjectionContainer {

    private Map<InjectionKey, InjectionMetaData> injectionMetaDataCache = new ConcurrentHashMap<InjectionKey, InjectionMetaData>();
    private SimpleInjection container;
    private InjectionFinder injectionFinder;
    private InstanceCreator instanceCreator;

    public AnnotationInjectionContainer(SimpleInjection container) {
        this.container = container;
    }

    public InjectionFinder getInjectionFinder() {
        return injectionFinder;
    }

    public InstanceCreator getInstanceCreator() {
        return instanceCreator;
    }

    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, String qualifier) {
        assert (qualifier != null);
        InjectionKey key = getNamedKey(qualifier, service);
        return getQualifiedService(service, forcedScope, key);
    }


    public <T> T getService(
            Class<T> service, SimpleInjection.Scope forcedScope, Class<? extends Annotation> qualifier) {
        assert (qualifier != null);
        InjectionKey key = getAnnotatedKey(qualifier, service);
        return getQualifiedService(service, forcedScope, key);
    }


    @SuppressWarnings(value = "unchecked")
    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope) {
        InjectionKey key = getKey(service);
        ServiceRegister serviceRegister = findServiceRegister(service, key);
        return (T) instantiateService(forcedScope, serviceRegister, key);
    }

    @SuppressWarnings(value = "unchecked")
    public <T, K> T getService(Class<T> service, K variable) {
        InjectionKey key = getNamedKey(VariableInjectionFactory.SERVICE_NAME, service);
        ServiceRegister serviceRegister = findServiceRegister(service, key);
        return (T) instantiateService(variable, serviceRegister, key);
    }

    @SuppressWarnings(value = "unchecked")
    private Object instantiateService(Object variable, ServiceRegister serviceRegister, InjectionKey key) {
        AnnotationInjection annotationInjection = new AnnotationInjection(injectionMetaDataCache, container, this);
        return annotationInjection.createInstance(serviceRegister.getService(), key, variable);
    }

    /**
     * Will find the service register but also perform registration where applicable
     *
     * @param service
     * @param key
     * @return the
     */
    public ServiceRegister findServiceRegister(Class service, InjectionKey key) {
        ServiceRegister serviceRegister = registeredNamedServices.get(key);
        if (serviceRegister == null && !service.isInterface()) {
            serviceRegister = register(key, false);
        } else if (serviceRegister == null && service.isInterface()) {
            serviceRegister = registerForInterface(key, false);
        }
        return serviceRegister;
    }


    @SuppressWarnings(value = "unchecked")
    public Object createInstance(ServiceRegister serviceRegister, InjectionKey key) {
        AnnotationInjection annotationInjection = new AnnotationInjection(injectionMetaDataCache, container, this);
        return annotationInjection.createInstance(serviceRegister.getService(), key);
    }

    public synchronized void register(
            InjectionKey key, Class service,
            SimpleInjection.Scope scope, SimpleInjection.RegisterType type, boolean throwError) {
        if (type != SimpleInjection.RegisterType.OVERRIDE_NORMAL && registeredNamedServices.containsKey(key)) {
            if (throwError) {
                throw new DuplicateRegistrationException("Can not overwrite an existing service with 'register', use 'overrideRegister'");
            }
            return;
        }
        register(key, service, scope);
    }

    public synchronized void register(SimpleInjection simpleInjection, RegistrationModule... modules) {
        for (RegistrationModule module : modules) {
            RegistrationModuleAnnotation aModule = (RegistrationModuleAnnotation) module;
            if (aModule instanceof RegistrationModuleAnnotationScanner) {
                ((RegistrationModuleAnnotationScanner) aModule).setSimpleInjection(simpleInjection);
            }
            if (aModule.getInjectionFinder() != null) {
                this.injectionFinder = aModule.getInjectionFinder();
            }
            if (aModule.getInstanceCreator() != null) {
                this.instanceCreator = aModule.getInstanceCreator();
            }
            aModule.preRegistration(container);
            for (RegistrationInstanceSimple instance : aModule.getRegistrations()) {
                InjectionKey key = instance.getInjectionKey();
                createAnStoreRegistration(instance, key, aModule);
            }
            aModule.postRegistration(container);
        }
    }


    public Class findService(InjectionKey key) {
        ServiceRegister serviceRegister = super.registeredNamedServices.get(key);
        if (serviceRegister != null) {
            return serviceRegister.getService();
        } else {
            throw new InjectRuntimeException("Service {0} with Name {1} not registered in SimpleInjection"
                    , key.getServiceDefinition(), key.getQualifier());
        }
    }

    public void injectDependencies(Object service) {
        AnnotationInjection annotationInjection = new AnnotationInjection(injectionMetaDataCache, container, this);
        annotationInjection.injectDependencies(service);
    }

    public void extendedInjectDependencies(Object service) {
        AnnotationInjection annotationInjection = new AnnotationInjection(injectionMetaDataCache, container, this);
        annotationInjection.injectExtendedDependencies(service);
    }


    @SuppressWarnings(value = "unchecked")
    private <T> T getQualifiedService(Class<T> service, SimpleInjection.Scope forcedScope, InjectionKey key) {
        if (!registeredNamedServices.containsKey(key) && service.getClass().isInterface()) {
            throw new InjectRuntimeException(
                    "Service {0} not registered in SimpleInjection and is an interface", service
            );
        }

        ServiceRegister serviceRegister = registeredNamedServices.get(key);
        if (serviceRegister == null && !service.isInterface()) {
            serviceRegister = register(key, service, null);
        }
        return (T) instantiateService(forcedScope, serviceRegister, key);
    }

    @SuppressWarnings(value = "unchecked")
    private ServiceRegister registerForInterface(InjectionKey key, boolean b) {
        Class serviceDefinition = key.getServiceDefinition();
        AnnotationInjection annotationInjection = new AnnotationInjection(injectionMetaDataCache, container, this);
        Class service = findServiceImplementation(serviceDefinition).getService();
        InjectionMetaData injectionMetaData = annotationInjection.findInjectionData(service, key);
        if (injectionMetaData != null) {
            register(key, injectionMetaData.getServiceClass(), null, null, false);
            return registeredNamedServices.get(key);
        } else {
            throw new InjectRuntimeException("No Service found for Interface {0}", serviceDefinition);
        }
    }

    private ServiceRegister register(InjectionKey key, Class service, SimpleInjection.Scope scope) {
        RegistrationInstanceSimple instance = new RegistrationInstanceSimple(service);
        instance.scopeAs(scope);
        if (key == null) {
            key = instance.getInjectionKey();
        }

        if (key.getAnnotation() != null) {
            instance.annotated(key.getAnnotation());
        } else if (key.getName() != null) {
            instance.named(key.getName());
        }

        return createAnStoreRegistration(instance, key, null);
    }

    private ServiceRegister register(InjectionKey key, boolean throwError) {
        register(key, key.getServiceDefinition(), null, null, throwError);
        return registeredNamedServices.get(key);
    }


    private ServiceRegister createAnStoreRegistration(RegistrationInstanceSimple instance,
                                                      InjectionKey key,
                                                      RegistrationModuleAnnotation aModule) {
        InjectionMetaData injectionMetaData = createInjectionMetaData(instance, key);
        ServiceRegister register = createServiceRegister(instance, injectionMetaData);
        register.setModule(aModule);
        putServiceIntoRegister(key, register);
        return register;

    }


    private ServiceRegister createServiceRegister(
            RegistrationInstanceSimple instance, InjectionMetaData injectionMetaData) {
        if (instance.getTheInstance() == null) {
            return new ServiceRegister(
                    instance.getService(),
                    null,
                    getAnnotationScope(injectionMetaData),
                    SimpleInjection.RegisterType.NORMAL
            );
        } else {
            return new ServiceRegister(
                    instance.getService(),
                    instance.getTheInstance(),
                    SimpleInjection.Scope.SINGLETON,
                    SimpleInjection.RegisterType.NORMAL
            );
        }
    }

    private InjectionMetaData createInjectionMetaData(RegistrationInstanceSimple instance, InjectionKey key) {
        InjectionMetaData injectionMetaData = createInjectionMetaData(instance.getService(), key);
        if (instance.getTheInstance() != null) {
            SingletonScopeHandler scopeHandler = new SingletonScopeHandler();
            scopeHandler.addScope(instance.getTheInstance());
            injectionMetaData.setScopeHandler(scopeHandler);
        } else if (instance.getTheFactory() != null) {
            FactoryScopeHandler scopeHandler = new FactoryScopeHandler(instance.getTheFactory());
            injectionMetaData.setScopeHandler(scopeHandler);
        } else if (instance.getTheVariableFactory() != null) {
            VariableFactoryScopeHandler scopeHandler = new VariableFactoryScopeHandler(instance.getTheVariableFactory());
            injectionMetaData.setScopeHandler(scopeHandler);
        } else if (instance.getScope() != null) {
            // replaces the injection data scope handler
            injectionMetaData.setScopeHandler(InjectionUtils.getScopeHandler(instance.getScope()));
        }
        return injectionMetaData;
    }

    private InjectionMetaData createInjectionMetaData(Class service, InjectionKey key) {
        AnnotationInjection annotationInjection = new AnnotationInjection(injectionMetaDataCache, container, this);
        return annotationInjection.createInjectionMetaData(service, key);
    }


    private SimpleInjection.Scope getAnnotationScope(InjectionMetaData injectionMetaData) {
        return injectionMetaData.getScope();
    }

    public Object clone(SimpleInjection simpleInjection) throws CloneNotSupportedException {
        AnnotationInjectionContainer annotationInjectionContainer = new AnnotationInjectionContainer(simpleInjection);

        annotationInjectionContainer.injectionMetaDataCache.putAll(this.injectionMetaDataCache);
        for (InjectionKey injectionKey : this.injectionMetaDataCache.keySet()) {
            InjectionMetaData injectionMetaData = this.injectionMetaDataCache.get(injectionKey);
            if (injectionMetaData.getScope() == ScopeContainer.Scope.SINGLETON) {
                annotationInjectionContainer.injectionMetaDataCache.put(injectionKey, injectionMetaData.clone());
            } else {
                annotationInjectionContainer.injectionMetaDataCache.put(injectionKey, injectionMetaData);
            }
        }

        for (InjectionKey injectionKey : this.registeredNamedServices.keySet()) {
            ServiceRegister serviceRegister = this.registeredNamedServices.get(injectionKey);
            if (serviceRegister.getScope() == ScopeContainer.Scope.SINGLETON) {
                annotationInjectionContainer.registeredNamedServices.put(injectionKey, serviceRegister.clone());
            } else {
                annotationInjectionContainer.registeredNamedServices.put(injectionKey, serviceRegister);
            }
        }
        return annotationInjectionContainer;
    }


}
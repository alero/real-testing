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

package org.hrodberaht.injection.core.internal.annotation;

import org.hrodberaht.injection.core.internal.InjectionContainerManager;
import org.hrodberaht.injection.core.internal.InjectionKey;
import org.hrodberaht.injection.core.internal.ScopeContainer;
import org.hrodberaht.injection.core.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.injection.core.internal.annotation.creator.InstanceCreatorDefault;
import org.hrodberaht.injection.core.internal.annotation.scope.ObjectAndScope;
import org.hrodberaht.injection.core.internal.exception.DependencyLocationError;
import org.hrodberaht.injection.core.internal.exception.DuplicateRegistrationException;
import org.hrodberaht.injection.core.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.core.internal.exception.InstanceCreationError;
import org.hrodberaht.injection.core.internal.stats.Statistics;
import org.hrodberaht.injection.core.register.VariableInjectionFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-maj-28 20:26:43
 * @version 1.0
 * @since 1.0
 */
public class AnnotationInjection {


    private InjectionCacheHandler injectionCacheHandler;
    private AnnotationInjectionContainer injectionContainer;
    private InjectionContainerManager container;
    private InjectionFinder injectionFinder = new DefaultInjectionPointFinder();
    private InstanceCreator instanceCreator = new InstanceCreatorDefault();

    AnnotationInjection(Map<InjectionKey, InjectionMetaDataBase> injectionMetaDataCache
            , InjectionContainerManager container
            , AnnotationInjectionContainer injectionContainer) {
        injectionCacheHandler = new InjectionCacheHandler(injectionMetaDataCache);
        this.container = container;
        this.injectionContainer = injectionContainer;
        InjectionFinder injectionFinderFromContainer = injectionContainer.getInjectionFinder();
        if (injectionFinderFromContainer != null) {
            this.injectionFinder = injectionFinderFromContainer;
        }
        InstanceCreator instanceCreatorFromContainer = injectionContainer.getInstanceCreator();
        if (instanceCreatorFromContainer != null) {
            this.instanceCreator = instanceCreatorFromContainer;
        }
    }

    /**
     * Creates an instance for a service, uses {@link #findInjectionData(Class, InjectionKey)}
     *
     * @param serviceDefinition
     * @param key
     * @return a created object according to its bound scope {@link javax.inject.Scope}
     */
    public Object createInstance(Class<Object> serviceDefinition, InjectionKey key) {
        InjectionMetaData injectionMetaData = findInjectionData(serviceDefinition, key);
        return callConstructor(injectionMetaData, false);
    }

    Object createNewInstance(Class<Object> serviceDefinition, InjectionKey key) {
        InjectionMetaData injectionMetaData = findInjectionData(serviceDefinition, key);
        return callConstructor(injectionMetaData, true);
    }

    public Object createInstance(Class serviceDefinition, InjectionKey key, Object variable) {
        InjectionMetaData variableInjectionMetaData = findInjectionData(serviceDefinition, key);
        Class serviceClass = variableInjectionMetaData.createVariableInstance(variable);
        InjectionMetaData injectionMetaData = findInjectionData(serviceClass, new InjectionKey(serviceClass, false));
        return callConstructor(injectionMetaData, false);
    }

    /**
     * Will register a predefined service for later resolve.
     * Sets the predefined attribute in the InjectionMetaData to true
     *
     * @param service
     * @param key
     * @param scope
     * @return a predefined services, not cached.
     */
    InjectionMetaData createInjectionMetaData(Class service, InjectionKey key, ScopeContainer.Scope scope) {
        InjectionMetaData injectionMetaData = new InjectionMetaData(service, key, instanceCreator);
        if (service != null) {
            Constructor constructor = InjectionUtils.findConstructor(service);
            injectionMetaData.setConstructor(constructor);
            injectionMetaData.setScopeHandler(InjectionUtils.getScopeHandler(injectionMetaData.getServiceClass(), scope));
        }
        injectionMetaData.setPreDefined(true);
        injectionCacheHandler.put(injectionMetaData);
        return injectionMetaData;
    }

    InjectionCacheHandler getInjectionCacheHandler() {
        return injectionCacheHandler;
    }

    /**
     * Checks the registry for the service class using a key, not null-safe.
     *
     * @param key the key to find a service class for
     * @return the found service class, null not accepted
     */
    Class findServiceClassAndRegister(InjectionKey key) {
        try {
            return this.injectionContainer.findServiceRegister(key.getServiceDefinition(), key).getService();
        } catch (DuplicateRegistrationException e) {
            return this.injectionContainer.findService(key);
        }
    }

    /**
     * Search for injectiondata in the cache, if not found creates new injection data from scratch.
     *
     * @param service the service implementation to register
     * @param key     the injection key (named or annotated service definition)
     * @return a cached injection meta data, is not protected from manipulation
     */
    InjectionMetaData findInjectionData(Class service, InjectionKey key) {
        InjectionMetaData cachedInjectionMetaData = injectionCacheHandler.find(
                new InjectionMetaData(service, key, instanceCreator));
        if (cachedInjectionMetaData != null) {
            if (cachedInjectionMetaData.isPreDefined() && !cachedInjectionMetaData.getKey().isProvider()) {
                resolvePredefinedService(cachedInjectionMetaData);
            }
            return cachedInjectionMetaData;
        }
        cachedInjectionMetaData = createInjectionMetaData(service, key, null);
        cachedInjectionMetaData.setPreDefined(false);
        resolveService(cachedInjectionMetaData);
        return cachedInjectionMetaData;
    }

    /**
     * @param dependency
     * @return
     */
    private Object innerCreateInstance(InjectionMetaData dependency) {
        if (dependency.getKey().isProvider()) {
            if (VariableInjectionFactory.SERVICE_NAME.equals(dependency.getKey().getName())) {
                return new VariableInjectionProvider(container, dependency.getKey());
            }
            return new InjectionProvider(container, dependency.getKey());
        }
        return createInstance(dependency);
    }

    /**
     * Resolves/finds all injection needs (constructors and members)
     *
     * @param injectionMetaData the service ready for resolving
     */
    private void resolveService(InjectionMetaData injectionMetaData) {
        try {
            List<InjectionMetaData> list = findDependencies(injectionMetaData.getConstructor());
            injectionMetaData.setConstructorDependencies(nullSafeIsEmpty(list) ? null : list);
            injectionMetaData.setInjectionPoints(
                    injectionFinder.findInjectionPoints(injectionMetaData.getServiceClass(), this)
            );
            injectionMetaData.setPostConstructMethod(
                    injectionFinder.findPostConstruct(injectionMetaData.getServiceClass())
            );
        } catch (DependencyLocationError dependencyLocationError) {
            throw new InjectRuntimeException("could not find dependencies for {0}", dependencyLocationError, injectionMetaData.getServiceClass().getName());
        }
    }

    private boolean nullSafeIsEmpty(List<InjectionMetaData> list) {
        if (list == null) {
            return true;
        }
        return list.isEmpty();
    }

    /**
     * Will resolve the service and set the predefined to false
     *
     * @param cachedInjectionMetaData a predefined service
     */
    private void resolvePredefinedService(InjectionMetaData cachedInjectionMetaData) {
        resolveService(cachedInjectionMetaData);
        cachedInjectionMetaData.setPreDefined(false);
    }


    public void injectDependencies(Object service) {
        InjectionMetaData injectionMetaData =
                findInjectionData(service.getClass(),
                        new InjectionKey(service.getClass(), false)
                );
        injectFromInjectionPoints(service, injectionMetaData);
    }

    private void injectFromInjectionPoints(Object service, InjectionMetaData injectionMetaData) {
        List<InjectionPoint> injectionPoints = injectionMetaData.getInjectionPoints();
        for (InjectionPoint injectionPoint : injectionPoints) {
            List<InjectionMetaData> dependencies = injectionPoint.getDependencies();
            if (injectionPoint.getType() == InjectionPoint.InjectionPointType.FIELD) {
                Object serviceDependence = innerCreateInstance(dependencies.get(0));
                injectionPoint.injectField(service, serviceDependence);
            } else {
                Object[] serviceDependencies = new Object[dependencies.size()];
                int i = 0;
                for (InjectionMetaData dependence : dependencies) {
                    Object serviceDependence = innerCreateInstance(dependence);
                    serviceDependencies[i] = serviceDependence;
                    i++;
                }
                injectionPoint.injectMethod(service, serviceDependencies);
            }
        }
    }

    /**
     * Uses the injection points to register instances for all services intended.
     *
     * @param service
     * @param injectionMetaData
     */
    private void autoWireBean(Object service, InjectionMetaData injectionMetaData) {
        injectFromInjectionPoints(service, injectionMetaData);
    }


    private List<InjectionMetaData> findDependencies(Constructor constructor) {
        if (constructor == null) {
            return Collections.emptyList();
        }
        Class[] parameterTypes = constructor.getParameterTypes();
        Type[] genericParameterTypes = constructor.getGenericParameterTypes();
        Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
        return InjectionUtils.findDependencies(parameterTypes, genericParameterTypes, parameterAnnotations, this);
    }


    private Object createInstance(InjectionMetaData metaData) {
        return createInstance(metaData.getServiceClass(), metaData.getKey());
    }


    private Object callConstructor(InjectionMetaData injectionMetaData, boolean enforceNew) {
        if (injectionMetaData.isExtendedInjection()) {
            return injectionMetaData.createInstance(null).getInstance();
        }
        List<InjectionMetaData> dependencies = injectionMetaData.getConstructorDependencies();
        if (dependencies == null) { // no constructor was able to be defined, hopefully a scoped one is provided.
            ObjectAndScope service = createInstanceObjectAndScope(injectionMetaData, enforceNew);
            if (service.isInNeedOfInitialization()) {
                return autowireAndPostConstruct(injectionMetaData, service.getInstance());
            }
            return service.getInstance();
        }
        Object[] servicesForConstructor = new Object[dependencies.size()];
        for (int i = 0; i < dependencies.size(); i++) {
            InjectionMetaData dependency = dependencies.get(i);
            Object bean = innerCreateInstance(dependency);
            servicesForConstructor[i] = bean;
            Statistics.addInjectConstructorCount();
        }
        ObjectAndScope service = createInstanceObjectAndScope(injectionMetaData, enforceNew, servicesForConstructor);
        if (service.isInNeedOfInitialization()) {
            return autowireAndPostConstruct(injectionMetaData, service.getInstance());
        }
        return service.getInstance();
    }

    private ObjectAndScope createInstanceObjectAndScope(InjectionMetaData injectionMetaData
            , boolean enforceNew) {
        try {
            if (enforceNew) {
                return injectionMetaData.createNewInstance(null);
            }
            return injectionMetaData.createInstance(null);
        } catch (InstanceCreationError e) {
            throw new InjectRuntimeException("could not create {0}", e, injectionMetaData.getServiceClass().getName());
        }

    }

    private ObjectAndScope createInstanceObjectAndScope(InjectionMetaData injectionMetaData
            , boolean enforceNew, Object[] dependencies) {
        if (enforceNew) {
            return injectionMetaData.createNewInstance(dependencies);
        }
        return injectionMetaData.createInstance(dependencies);
    }

    private Object autowireAndPostConstruct(InjectionMetaData injectionMetaData, final Object service) {
        autoWireBean(service, injectionMetaData);
        final Object newService = injectionFinder.extendedInjection(service);
        Method postConstruct = injectionMetaData.getPostConstruct();
        if (postConstruct != null) {
            ReflectionUtils.invoke(postConstruct, newService);
        }
        return newService;
    }

    public void autowireAndPostConstruct(Object service) {
        InjectionMetaData injectionMetaData =
                findInjectionData(service.getClass(),
                        new InjectionKey(service.getClass(), false)
                );
        autowireAndPostConstruct(injectionMetaData, service);
    }
}

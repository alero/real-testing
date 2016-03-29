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

package org.hrodberaht.injection.internal.annotation;

import org.hrodberaht.injection.InjectionContainerManager;
import org.hrodberaht.injection.ScopeContainer;
import org.hrodberaht.injection.internal.InjectionKey;
import org.hrodberaht.injection.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.injection.internal.annotation.creator.InstanceCreatorDefault;
import org.hrodberaht.injection.internal.annotation.scope.ObjectAndScope;
import org.hrodberaht.injection.internal.exception.DuplicateRegistrationException;
import org.hrodberaht.injection.internal.stats.Statistics;
import org.hrodberaht.injection.register.VariableInjectionFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-28 20:26:43
 * @version 1.0
 * @since 1.0
 */
public class AnnotationInjection {


    private InjectionCacheHandler injectionCacheHandler;
    private AnnotationInjectionContainer injectionContainer;
    private InjectionContainerManager container;
    private InjectionFinder injectionFinder = new DefaultInjectionPointFinder();
    private InstanceCreator instanceCreator = new InstanceCreatorDefault();

    public AnnotationInjection(Map<InjectionKey, InjectionMetaDataBase> injectionMetaDataCache
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

    public Object createNewInstance(Class<Object> serviceDefinition, InjectionKey key) {
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
     * Will create a predefined service for later resolve.
     * Sets the predefined attribute in the InjectionMetaData to true
     *
     * @param service
     * @param key
     * @param scope
     * @return a predefined services, not cached.
     */
    public InjectionMetaData createInjectionMetaData(Class service, InjectionKey key, ScopeContainer.Scope scope) {
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

    public InjectionCacheHandler getInjectionCacheHandler(){
        return injectionCacheHandler;
    }

    public void removeInjectionMetaData(Class service, InjectionKey key, ScopeContainer.Scope scope) {
        InjectionMetaData injectionMetaData = new InjectionMetaData(service, key, instanceCreator);
        injectionCacheHandler.clear(injectionMetaData);
    }

    /**
     * Checks the registry for the service class using a key, not null-safe.
     *
     * @param key the key to find a service class for
     * @return the found service class, null not accepted
     */
    public Class findServiceClassAndRegister(InjectionKey key) {
        try {
            return this.injectionContainer.findServiceRegister(key.getServiceDefinition(), key).getService();
        } catch (DuplicateRegistrationException e) {
            return this.injectionContainer.findService(key);
        }
    }

    /**
     * Search for injectiondata in the cache, if not found creates new injection data from scratch.
     *
     * @param service the service implementation to create
     * @param key     the injection key (named or annotated service definition)
     * @return a cached injection meta data, is not protected from manipulation
     */
    public InjectionMetaData findInjectionData(Class service, InjectionKey key) {
        InjectionMetaData cachedInjectionMetaData = injectionCacheHandler.find(
                new InjectionMetaData(service, key, instanceCreator));
        if (cachedInjectionMetaData != null) {
            if (cachedInjectionMetaData.isPreDefined()) {
                if (!cachedInjectionMetaData.getKey().isProvider()) {
                    resolvePredefinedService(cachedInjectionMetaData);
                }
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
    private Object innerCreateInstance(InjectionMetaData dependency, boolean enforceNew) {
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
        List<InjectionMetaData> list = findDependencies(injectionMetaData.getConstructor());
        injectionMetaData.setConstructorDependencies(validateListSize(list) ? null : list);
        injectionMetaData.setInjectionPoints(
                injectionFinder.findInjectionPoints(injectionMetaData.getServiceClass(), this)
        );
        injectionMetaData.setPostConstructMethod(
                injectionFinder.findPostConstruct(injectionMetaData.getServiceClass())
        );
    }

    private boolean validateListSize(List<InjectionMetaData> list) {
        if(list == null){
            return true;
        }
        return list.size() == 0;
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
                Object serviceDependence = innerCreateInstance(dependencies.get(0), false);
                injectionPoint.injectField(service, serviceDependence);
            } else {
                Object[] serviceDependencies = new Object[dependencies.size()];
                int i = 0;
                for (InjectionMetaData dependence : dependencies) {
                    Object serviceDependence = innerCreateInstance(dependence, false);
                    serviceDependencies[i] = serviceDependence;
                    i++;
                }
                injectionPoint.inject(service, serviceDependencies);
            }
        }
    }

    /**
     * Uses the injection points to create instances for all services intended.
     *
     * @param service
     * @param injectionMetaData
     */
    private void autoWireBean(Object service, InjectionMetaData injectionMetaData) {
        injectFromInjectionPoints(service, injectionMetaData);
    }


    private List<InjectionMetaData> findDependencies(Constructor constructor) {
        if (constructor == null) {
            return null;
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
            Object bean = innerCreateInstance(dependency, enforceNew);
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
        if(enforceNew){
            return injectionMetaData.createNewInstance(null);
        }
        return injectionMetaData.createInstance(null);
    }

    private ObjectAndScope createInstanceObjectAndScope(InjectionMetaData injectionMetaData
            , boolean enforceNew, Object[] dependencies) {
        if(enforceNew){
            return injectionMetaData.createNewInstance(dependencies);
        }
        return injectionMetaData.createInstance(dependencies);
    }

    private Object autowireAndPostConstruct(InjectionMetaData injectionMetaData, Object service) {
        autoWireBean(service, injectionMetaData);
        injectionFinder.extendedInjection(service);
        Method postConstruct = injectionMetaData.getPostConstruct();
        if (postConstruct != null) {
            ReflectionUtils.invoke(postConstruct, service);
        }
        return service;
    }


    public void injectExtendedDependencies(Object service) {
        /*InjectionMetaData injectionMetaData =
                findInjectionData(service.getClass(),
                        new InjectionKey(service.getClass(), false)
                );
        List<InjectionPoint> injectionPoints = injectionMetaData.getInjectionPoints();
        for (InjectionPoint injectionPoint : injectionPoints) {
            List<InjectionMetaData> dependencies = injectionPoint.getDependencies();
            Object[] serviceDependencies = new Object[dependencies.size()];
            int i = 0;
            // for (InjectionMetaData dependence : dependencies) {
                // Object serviceDependence = innerCreateInstance(dependence);
                //  serviceDependencies[i] = serviceDependence;
                //i++;
            //}
            injectionFinder.extendedInjection(service); // inject services that are pre-created
            injectionPoint.inject(service, serviceDependencies);
        } */
    }


}

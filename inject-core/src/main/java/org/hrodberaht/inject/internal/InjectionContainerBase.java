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

package org.hrodberaht.inject.internal;

import org.hrodberaht.inject.SimpleInjection;
import org.hrodberaht.inject.internal.exception.InjectRuntimeException;
import org.hrodberaht.inject.internal.stats.Statistics;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-29 13:07:33
 * @version 1.0
 * @since 1.0
 */
public abstract class InjectionContainerBase {


    public Collection<ServiceRegister> getServiceRegister() {
        Collection<ServiceRegister> registry = new ArrayList<ServiceRegister>(50);
        registry.addAll(getNamedRegisteredServices());
        return registry;
    }

    protected Map<InjectionKey, ServiceRegister>
            registeredNamedServices = new ConcurrentHashMap<InjectionKey, ServiceRegister>();

    protected InjectionKey getNamedKey(String qualifier, Class serviceDefinition) {
        return new InjectionKey(qualifier, serviceDefinition, false);
    }

    protected InjectionKey getAnnotatedKey(Class<? extends Annotation> qualifier, Class serviceDefinition) {
        return new InjectionKey(qualifier, serviceDefinition, false);
    }

    protected InjectionKey getKey(Class serviceDefinition) {
        return new InjectionKey(serviceDefinition, false);
    }

    protected Object instantiateService(
            SimpleInjection.Scope forcedScope, ServiceRegister serviceRegister, InjectionKey key) {
        if (forcedScope == null && serviceRegister.getScope() == SimpleInjection.Scope.NEW) {
            return createInstance(serviceRegister, key);
        } else if (SimpleInjection.Scope.NEW == forcedScope) {
            return createInstance(serviceRegister, key);
        }
        if (serviceRegister.getSingleton() == null) {
            serviceRegister.setSingleton(createInstance(serviceRegister, key));
        }
        return serviceRegister.getSingleton();
    }

    protected static SimpleInjection.RegisterType normalizeType(SimpleInjection.RegisterType type) {
        if (type == SimpleInjection.RegisterType.OVERRIDE_NORMAL) {
            return SimpleInjection.RegisterType.NORMAL;
        }
        return type;
    }

    protected abstract Object createInstance(ServiceRegister serviceRegister, InjectionKey key);

    protected <T> ServiceRegister findServiceImplementation(Class<T> service) {
        InjectionKey key = getKey(service);
        if (!registeredNamedServices.containsKey(key)) {
            if (service.isInterface()) {
                ServiceRegister foundServiceRegister = null;
                for (ServiceRegister serviceRegister : registeredNamedServices.values()) {
                    if (service.isAssignableFrom(serviceRegister.getService())) {
                        if (foundServiceRegister == null) {
                            foundServiceRegister = serviceRegister;
                        } else {
                            throw new InjectRuntimeException("Found two Implementations \"{0}\", \"{1}\" " +
                                    "matching the Interface \"{2}\"" +
                                    ". This normally occurs when scanning implementations and can be corrected " +
                                    " by manually registering one of them to the active Interface",
                                    foundServiceRegister.getService(),
                                    serviceRegister.getService()
                                    , service);
                        }
                    }
                }
                if (foundServiceRegister != null) {
                    return foundServiceRegister;
                }
            }
            throw new InjectRuntimeException("Service {0} not registered in SimpleInjection", service);
        }
        return registeredNamedServices.get(key);
    }

    protected void putServiceIntoRegister(InjectionKey key, ServiceRegister register) {
        ServiceRegister oldRegister = registeredNamedServices.get(key);
        register.setOverriddenService(oldRegister);
        registeredNamedServices.put(key, register);
        Statistics.addRegisterServicesCount();
    }

    private Collection<ServiceRegisterNamed> getNamedRegisteredServices() {
        Collection<ServiceRegisterNamed> regulars = new ArrayList<ServiceRegisterNamed>();
        Collection<InjectionKey> keys = registeredNamedServices.keySet();
        for (InjectionKey registerKey : keys) {
            ServiceRegister register = registeredNamedServices.get(registerKey);
            ServiceRegisterNamed registerRegular = new ServiceRegisterNamed(register);
            registerRegular.setKey(registerKey);
            regulars.add(registerRegular);
        }
        return regulars;
    }
}

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

package org.hrodberaht.injection.internal;

import org.hrodberaht.injection.internal.annotation.InjectionMetaDataBase;
import org.hrodberaht.injection.internal.annotation.ServiceRegistryForInjection;
import org.hrodberaht.injection.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.internal.stats.Statistics;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-maj-29 13:07:33
 * @version 1.0
 * @since 1.0
 */
public abstract class InjectionContainerBase {

    protected ServiceRegistryForInjection registeredServices;

    public Collection<ServiceRegister> getServiceRegister() {
        return registeredServices.getServiceRegisterCollection();
    }

    public Collection<InjectionMetaDataBase> getInjectionMetaDataBaseCollection() {
        return registeredServices.getInjectionMetaDataBaseCollection();
    }

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
            InjectionContainerManager.Scope forcedScope, ServiceRegister serviceRegister, InjectionKey key) {
        if (forcedScope == null && serviceRegister.getScope() == InjectionContainerManager.Scope.NEW) {
            return createInstance(serviceRegister, key);
        } else if (InjectionContainerManager.Scope.NEW == forcedScope) {
            return createNewInstance(serviceRegister, key);
        }
        if (serviceRegister.getSingleton() == null) {
            serviceRegister.setSingleton(createInstance(serviceRegister, key));
        }
        return serviceRegister.getSingleton();
    }


    protected static InjectionContainerManager.RegisterType normalizeType(InjectionContainerManager.RegisterType type) {
        if (type == InjectionContainerManager.RegisterType.OVERRIDE_NORMAL) {
            return InjectionContainerManager.RegisterType.NORMAL;
        }
        return type;
    }

    protected abstract Object createNewInstance(ServiceRegister serviceRegister, InjectionKey key);

    protected abstract Object createInstance(ServiceRegister serviceRegister, InjectionKey key);

    protected <T> ServiceRegister findServiceImplementation(Class<T> service) {
        InjectionKey key = getKey(service);
        if (!registeredServices.containsKey(key)) {
            if (service.isInterface()) {
                ServiceRegister foundServiceRegister = null;
                for (ServiceRegister serviceRegister : registeredServices.getServiceRegisterCollection()) {
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
        return registeredServices.get(key);
    }

    protected void putServiceIntoRegister(InjectionKey key, ServiceRegister register) {
        ServiceRegister oldRegister = registeredServices.get(key);
        register.setOverriddenService(oldRegister);
        registeredServices.put(key, register);
        Statistics.addRegisterServicesCount();
    }


}

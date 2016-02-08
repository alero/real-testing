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
import org.hrodberaht.inject.internal.exception.DuplicateRegistrationException;
import org.hrodberaht.inject.internal.exception.InjectRuntimeException;
import org.hrodberaht.inject.register.RegistrationModule;
import org.hrodberaht.inject.register.internal.RegistrationInstanceSimple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-29 12:38:22
 * @version 1.0
 * @since 1.0
 */
public class SimpleInjectionContainer extends InjectionContainerBase
        implements InjectionContainer, RegistrationInjectionContainer {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleInjectionContainer.class);

    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, String qualifier) {
        InjectionKey key = getNamedKey(qualifier, service);
        return getQualifiedService(service, forcedScope, key);
    }

    public <T> T getService(
            Class<T> service, SimpleInjection.Scope forcedScope, Class<? extends Annotation> qualifier) {
        InjectionKey key = getAnnotatedKey(qualifier, service);
        return getQualifiedService(service, forcedScope, key);
    }

    @SuppressWarnings(value = "unchecked")
    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope) {
        ServiceRegister serviceRegister = findServiceImplementation(service);
        return (T) instantiateService(forcedScope, serviceRegister, null);
    }

    public <T, K> T getService(Class<T> service, K variable) {
        return null;
    }


    public void register(
            InjectionKey key, Class service, SimpleInjection.Scope scope,
            SimpleInjection.RegisterType type, boolean throwError) {
        if (registeredNamedServices.containsKey(key)) {
            reRegisterSupport(key, type, throwError);
        }
        LOG.debug("Putting service into registry for key={}", key);
        putServiceIntoRegister(key,
                new ServiceRegister(service,
                        createInstance(new ServiceRegister(service), key)
                        , scope, normalizeType(type)
                )
        );

    }

    @SuppressWarnings(value = "unchecked")
    public void register(SimpleInjection simpleInjection, RegistrationModule... modules) {
        for (RegistrationModule<RegistrationInstanceSimple> module : modules) {
            module.preRegistration(null);
            for (RegistrationInstanceSimple instance : module.getRegistrations()) {
                InjectionKey key = instance.getInjectionKey();
                register(instance, key);
            }
            module.postRegistration(null);
        }
    }

    private void register(RegistrationInstanceSimple instance, InjectionKey key) {
        register(key, instance.getService(), instance.getScope(), instance.getRegisterType(), false);
    }

    @SuppressWarnings(value = "unchecked")
    private <T> T getQualifiedService(Class<T> service, SimpleInjection.Scope forcedScope, InjectionKey key) {
        if (!registeredNamedServices.containsKey(key)) {
            throw new InjectRuntimeException("Service {0} not registered in SimpleInjection", service);
        }
        ServiceRegister serviceRegister = registeredNamedServices.get(key);
        if (serviceRegister == null && !service.getClass().isInterface()) {
            register(key, service, forcedScope, SimpleInjection.RegisterType.NORMAL, false);
            serviceRegister = registeredNamedServices.get(key);
        }
        return (T) instantiateService(forcedScope, serviceRegister, key);
    }

    private void reRegisterSupport(InjectionKey key, SimpleInjection.RegisterType type, boolean throwError) {
        ServiceRegister serviceRegister = registeredNamedServices.get(key);
        if (serviceRegister.getRegisterType() == SimpleInjection.RegisterType.WEAK) {
            // registeredNamedServices.remove(key);
            return;
        }

        if (serviceRegister.getRegisterType() == SimpleInjection.RegisterType.NORMAL) {
            if (type == SimpleInjection.RegisterType.OVERRIDE_NORMAL) {
                // registeredNamedServices.remove(key);
                return;
            }
            throwRegistrationError("a Service",
                    "to override register please use overrideRegister method", key, throwError);
        }
        if (serviceRegister.getRegisterType() == SimpleInjection.RegisterType.FINAL) {
            throwRegistrationError("a FINAL Service", "can not perform override registration", key, throwError);
        }

    }

    private void throwRegistrationError(String message, String help, InjectionKey key, boolean throwError) {
        String qualifier = key.getQualifier();
        if (!throwError) {
            return;
        }
        if (qualifier != null) {
            throw new DuplicateRegistrationException(
                    message + " for qualifier \"{0}\" and serviceDefinition \"{1}\" " +
                            "is already registered, " + help, qualifier, key.getServiceDefinition());
        } else {
            throw new DuplicateRegistrationException(
                    message + " for serviceDefinition \"{0}\" " +
                            "is already registered, " + help, key.getServiceDefinition());
        }
    }

    public Object createInstance(ServiceRegister serviceRegister, InjectionKey key) {
        try {
            return serviceRegister.getService().newInstance(); // uses empty constructor
        } catch (InstantiationException e) {
            throw new InjectRuntimeException("Could not create an instance of {0}", e, serviceRegister.getService());
        } catch (IllegalAccessException e) {
            throw new InjectRuntimeException("Could not create an instance of {0}", e, serviceRegister.getService());
        }
    }

    public Object clone(SimpleInjection injection) throws CloneNotSupportedException {
        return super.clone();
    }
}

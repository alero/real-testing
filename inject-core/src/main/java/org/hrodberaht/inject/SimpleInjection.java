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

package org.hrodberaht.inject;

import org.hrodberaht.inject.internal.InjectionContainer;
import org.hrodberaht.inject.internal.InjectionKey;
import org.hrodberaht.inject.internal.RegistrationInjectionContainer;
import org.hrodberaht.inject.internal.ServiceRegister;
import org.hrodberaht.inject.internal.SimpleInjectionContainer;
import org.hrodberaht.inject.internal.annotation.AnnotationInjectionContainer;
import org.hrodberaht.inject.register.RegistrationModule;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Simple Java Utils - Container - Container
 *
 * @author Robert Alexandersson
 *         2010-mar-27 14:05:34
 * @version 1.0
 * @since 1.0
 */
public class SimpleInjection implements Container, ScopeContainer, InjectContainer, ExtendedInjection, ExtendedAnnotationInjection {


    private InjectionContainer injectionContainer = new AnnotationInjectionContainer(this);

    public Collection<ServiceRegister> getServiceRegister() {
        return injectionContainer.getServiceRegister();
    }

    public AnnotationInjectionContainer getAnnotatedContainer() {
        return (AnnotationInjectionContainer) injectionContainer;
    }

    public enum RegisterType {
        WEAK, NORMAL, OVERRIDE_NORMAL, FINAL
    }


    /**
     * Will retrieve a service as it has been registered,
     * scope's supported today are {@link SimpleInjection.Scope#SINGLETON} and {@link SimpleInjection.Scope#NEW}
     *
     * @param service the interface service intended for creation
     * @param <T>     the typed service intended for creation
     * @return an instance of the interface requested will be created/fetched and returned from the internal register
     */
    public <T> T get(Class<T> service) {
        return injectionContainer.getService(service, null);
    }

    /**
     * Will retrieve a service as it has been registered,
     * scope's supported today are {@link SimpleInjection.Scope#SINGLETON} and {@link SimpleInjection.Scope#NEW}
     *
     * @param service   the interface service intended for creation
     * @param qualifier the named service intended for creation
     * @return an instance of the interface requested will be created/fetched and returned from the internal register
     */
    public <T> T get(Class<T> service, String qualifier) {
        if (qualifier == null || "".equals(qualifier)) {
            return injectionContainer.getService(service, null);
        }
        return injectionContainer.getService(service, null, qualifier);
    }

    /**
     * Will retrieve a service as it has been registered,
     * scope's supported today are {@link SimpleInjection.Scope#SINGLETON} and {@link SimpleInjection.Scope#NEW}
     *
     * @param service   the interface service intended for creation
     * @param qualifier the named service intended for creation
     * @return an instance of the interface requested will be created/fetched and returned from the internal register
     */
    public <T> T get(Class<T> service, Class<? extends Annotation> qualifier) {
        if (qualifier == null) {
            return injectionContainer.getService(service, null);
        }
        return injectionContainer.getService(service, null, qualifier);
    }

    public <T, K> T get(Class<T> service, K variable) {
        return injectionContainer.getService(service, variable);
    }

    /**
     * Will retrieve a service and force the scope to {@link SimpleInjection.Scope#NEW}
     *
     * @param service the interface service intended for creation
     * @param <T>     the typed service intended for creation
     * @return an instance of the interface requested will be created/fetched and returned from the internal register
     */
    public <T> T getNew(Class<T> service) {
        return injectionContainer.getService(service, Scope.NEW);
    }

    /**
     * Will retrieve a service and force the scope to {@link SimpleInjection.Scope#SINGLETON}
     *
     * @param service the interface service intended for creation
     * @param <T>     the typed service intended for creation
     * @return an instance of the interface requested will be created/fetched and returned from the internal register
     */
    public <T> T getSingleton(Class<T> service) {
        return injectionContainer.getService(service, Scope.SINGLETON);
    }


    protected synchronized void register(Class serviceDefinition, Class service, Scope scope, RegisterType type) {
        if (injectionContainer instanceof RegistrationInjectionContainer) {
            RegistrationInjectionContainer container = (RegistrationInjectionContainer) injectionContainer;
            container.register(new InjectionKey(serviceDefinition, false), service, scope, type, true);
        }
    }

    protected synchronized void register(InjectionKey key, Class service, Scope scope, RegisterType type) {
        if (injectionContainer instanceof RegistrationInjectionContainer) {
            RegistrationInjectionContainer container = (RegistrationInjectionContainer) injectionContainer;
            container.register(key, service, scope, type, true);
        }
    }

    public void register(RegistrationModule... modules) {
        if (injectionContainer instanceof RegistrationInjectionContainer) {
            RegistrationInjectionContainer container = (RegistrationInjectionContainer) injectionContainer;
            container.register(this, modules);
        }
    }

    protected synchronized void setContainerInjectAnnotationCompliantMode() {
        injectionContainer = new AnnotationInjectionContainer(this);
    }

    protected synchronized void setContainerSimpleInjection() {
        injectionContainer = new SimpleInjectionContainer();
    }

    protected synchronized InjectionContainer getContainer() {
        // TODO: make this support clone or remove
        return injectionContainer;
    }

    public void injectDependencies(Object service) {
        if (injectionContainer instanceof AnnotationInjectionContainer) {
            AnnotationInjectionContainer annotationContainer = (AnnotationInjectionContainer) injectionContainer;
            annotationContainer.injectDependencies(service);
        } else {
            throw new IllegalAccessError("Method not supported by InjectionContainer typed: "
                    + injectionContainer.getClass().getName());
        }

    }

    public void injectExtendedDependencies(Object service) {
        if (injectionContainer instanceof AnnotationInjectionContainer) {
            AnnotationInjectionContainer annotationContainer = (AnnotationInjectionContainer) injectionContainer;
            annotationContainer.extendedInjectDependencies(service);
        } else {
            throw new IllegalAccessError("Method not supported by InjectionContainer typed: "
                    + injectionContainer.getClass().getName());
        }
    }

    @Override
    public SimpleInjection clone() throws CloneNotSupportedException {
        SimpleInjection simpleInjection = new SimpleInjection();
        simpleInjection.injectionContainer = (InjectionContainer) this.injectionContainer.clone(simpleInjection);
        return simpleInjection;
    }
}
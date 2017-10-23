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

package org.hrodberaht.injection.internal;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.internal.annotation.AnnotationInjectionContainer;
import org.hrodberaht.injection.register.RegistrationModule;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Simple Java Utils - Container - Container
 *
 * @author Robert Alexandersson
 * 2010-mar-27 14:05:34
 * @version 1.0
 * @since 1.0
 */
public class InjectionContainerManager implements ScopeContainer, InjectContainer, ExtendedAnnotationInjection {

    private AnnotationInjectionContainer injectionContainer = new AnnotationInjectionContainer(this);

    public Collection<ServiceRegister> getServiceRegister() {
        return injectionContainer.getServiceRegister();
    }

    public AnnotationInjectionContainer getAnnotatedContainer() {
        return injectionContainer;
    }

    public enum RegisterType {
        WEAK, NORMAL, OVERRIDE_NORMAL, FINAL
    }


    /**
     * Will retrieve a service as it has been registered,
     * scope's supported today are {@link Scope#SINGLETON} and {@link Scope#NEW}
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
     * scope's supported today are {@link Scope#SINGLETON} and {@link Scope#NEW}
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
     * scope's supported today are {@link Scope#SINGLETON} and {@link Scope#NEW}
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
     * Will retrieve a service and force the scope to {@link Scope#NEW}
     *
     * @param service the interface service intended for creation
     * @param <T>     the typed service intended for creation
     * @return an instance of the interface requested will be created/fetched and returned from the internal register
     */
    public <T> T getNew(Class<T> service) {
        return injectionContainer.getService(service, Scope.NEW);
    }

    /**
     * Will retrieve a service and force the scope to {@link Scope#SINGLETON}
     *
     * @param service the interface service intended for creation
     * @param <T>     the typed service intended for creation
     * @return an instance of the interface requested will be created/fetched and returned from the internal register
     */
    public <T> T getSingleton(Class<T> service) {
        return injectionContainer.getService(service, Scope.SINGLETON);
    }


    protected void register(Class serviceDefinition, Class service, Scope scope, RegisterType type) {
        injectionContainer.register(new InjectionKey(serviceDefinition, false), service, scope, type, true);
    }

    protected void register(InjectionKey key, Class service, Scope scope, RegisterType type) {
        injectionContainer.register(key, service, scope, type, true);
    }

    public void register(RegistrationModule... modules) {
        injectionContainer.register(this, modules);
    }

    protected InjectionContainer getContainer() {
        // TODO: make this support clone or remove
        return injectionContainer;
    }

    @Override
    public void autowireAndPostConstruct(Object service) {
        injectionContainer.autowireAndPostConstruct(service);
    }

    public void injectDependencies(Object service) {
        injectionContainer.injectDependencies(service);
    }

    public InjectionContainerManager copy() {
        InjectionContainerManager injectionContainerManager = new InjectionContainerManager();
        injectionContainerManager.injectionContainer = this.injectionContainer.copy(injectionContainerManager);
        return injectionContainerManager;
    }
}
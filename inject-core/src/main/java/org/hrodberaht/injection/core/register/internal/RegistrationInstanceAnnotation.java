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

package org.hrodberaht.injection.core.register.internal;

import org.hrodberaht.injection.core.internal.InjectionContainerManager;
import org.hrodberaht.injection.core.internal.InjectionKey;
import org.hrodberaht.injection.core.internal.ScopeContainer;
import org.hrodberaht.injection.core.register.InjectionFactory;
import org.hrodberaht.injection.core.register.SimpleInjectionFactory;
import org.hrodberaht.injection.core.register.VariableInjectionFactory;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-jun-03 17:53:13
 * @version 1.0
 * @since 1.0
 */
public class RegistrationInstanceAnnotation<T extends Registration> implements Registration {

    protected Class theInterface;

    // Producers
    protected Class theService;
    protected Object theInstance;
    protected String name;
    protected Class<? extends Annotation> annotation;
    protected InjectionContainerManager.Scope scope = null; // No default scope for registration
    private InjectionFactory theFactory;
    private VariableInjectionFactory theVariableFactory;


    public RegistrationInstanceAnnotation(Class theInterface) {
        this.theInterface = theInterface;
        this.theService = theInterface;
    }

    @SuppressWarnings(value = "unchecked")
    public T annotated(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
        return (T) this;
    }

    @SuppressWarnings(value = "unchecked")
    public T named(String named) {
        this.name = named;
        return (T) this;
    }

    public T with(Class theService) {
        this.theService = theService;
        return (T) this;
    }

    public T withInstance(Object theInstance) {
        this.theInstance = theInstance;
        this.theService = theInstance.getClass();
        this.scope = ScopeContainer.Scope.SINGLETON;
        return (T) this;
    }

    public T withFactory(InjectionFactory aFactory) {
        this.theFactory = aFactory;
        this.theService = theFactory.getInstanceType();
        this.name = theFactory.name();
        this.scope = ScopeContainer.Scope.NEW;
        return (T) this;
    }

    public T withFactoryInstance(Object theInstance) {
        this.theFactory = new SimpleInjectionFactory<>(theInstance);
        this.theService = theFactory.getInstanceType();
        this.scope = ScopeContainer.Scope.NEW;
        return (T) this;
    }

    public T withVariableFactory(VariableInjectionFactory variableInjectionFactory) {
        this.theVariableFactory = variableInjectionFactory;
        this.name = variableInjectionFactory.name();
        this.scope = ScopeContainer.Scope.NEW;
        return (T) this;
    }


    public Class getService() {
        return theService;
    }

    public Object getTheInstance() {
        return theInstance;
    }

    public InjectionFactory getTheFactory() {
        return theFactory;
    }

    public VariableInjectionFactory getTheVariableFactory() {
        return theVariableFactory;
    }

    public T scopeAs(ScopeContainer.Scope scope) {
        this.scope = scope;
        return (T) this;
    }

    public ScopeContainer.Scope getScope() {
        return scope;
    }


    public InjectionKey getInjectionKey() {
        if (annotation != null) {
            return new InjectionKey(annotation, theInterface, false);
        } else if (name != null) {
            return new InjectionKey(name, theInterface, false);
        }
        return new InjectionKey(theInterface, false);
    }


}
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

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.register.InjectionRegister;

import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-28 21:12:27
 * @version 1.0
 * @since 1.0
 */
public abstract class InjectionRegisterBase<T extends InjectionRegister> implements InjectionRegister {

    protected InjectionContainerManager container = null;

    public InjectionContainerManager getInnerContainer() {
        return container;
    }

    protected InjectionRegisterBase() {
        this.container = new InjectionContainerManager();
    }

    protected InjectionRegisterBase(InjectionRegister register) {
        this.container = register.getInnerContainer();
    }


    public InjectionRegister register(Class<? extends Annotation> qualifier, Class serviceDefinition, Class service) {
        register(qualifier, serviceDefinition, service, InjectionContainerManager.Scope.NEW);
        return this;
    }

    public InjectionRegister register(Class serviceDefinition, Class service) {
        register(serviceDefinition, service, InjectionContainerManager.Scope.NEW);
        return this;
    }

    public InjectionRegister register(Class service) {
        register(service, service, InjectionContainerManager.Scope.NEW);
        return this;
    }

    public InjectionRegister overrideRegister(Class<? extends Annotation> qualifier, Class serviceDefinition, Class service) {
        overrideRegister(qualifier, serviceDefinition, service, InjectionContainerManager.Scope.NEW);
        return this;
    }

    public InjectionRegister overrideRegister(Class serviceDefinition, Class service) {
        overrideRegister(serviceDefinition, service, InjectionContainerManager.Scope.NEW);
        return this;
    }

    public InjectionRegister overrideRegister(Class service) {
        overrideRegister(service, service, InjectionContainerManager.Scope.NEW);
        return this;
    }

    public InjectionRegister finalRegister(Class<? extends Annotation> qualifier, Class serviceDefinition, Class service) {
        finalRegister(qualifier, serviceDefinition, service, InjectionContainerManager.Scope.NEW);
        return this;
    }


    public InjectionRegister finalRegister(Class serviceDefinition, Class service) {
        finalRegister(serviceDefinition, service, InjectionContainerManager.Scope.NEW);
        return this;
    }

    public InjectionRegister finalRegister(Class service) {
        finalRegister(service, service, InjectionContainerManager.Scope.NEW);
        return this;
    }

    public InjectContainer getContainer() {
        return container;
    }

    public ScopeContainer getScopedContainer() {
        return container;
    }

    protected InjectionRegister finalRegister(Class serviceDefinition, Class service, InjectionContainerManager.Scope scope) {
        container.register(serviceDefinition, service, scope, InjectionContainerManager.RegisterType.FINAL);
        return this;
    }

    public InjectionRegister overrideRegister(Class serviceDefinition, Class service, InjectionContainerManager.Scope scope) {
        container.register(serviceDefinition, service, scope, InjectionContainerManager.RegisterType.OVERRIDE_NORMAL);
        return this;
    }

    protected InjectionRegister register(Class serviceDefinition, Class service, InjectionContainerManager.Scope scope) {
        container.register(serviceDefinition, service, scope, InjectionContainerManager.RegisterType.NORMAL);
        return this;
    }

    protected InjectionRegister register(
            Class<? extends Annotation> qualifier,
            Class serviceDefinition,
            Class service, InjectionContainerManager.Scope scope) {
        container.register(
                new InjectionKey(qualifier, serviceDefinition, false)
                , service, scope, InjectionContainerManager.RegisterType.NORMAL
        );
        return this;
    }

    protected InjectionRegister overrideRegister(
            Class<? extends Annotation> qualifier,
            Class serviceDefinition,
            Class service, InjectionContainerManager.Scope scope) {
        container.register(
                new InjectionKey(qualifier, serviceDefinition, false)
                , service, scope, InjectionContainerManager.RegisterType.OVERRIDE_NORMAL
        );
        return this;
    }

    private InjectionRegister finalRegister(
            Class<? extends Annotation> qualifier,
            Class serviceDefinition,
            Class service,
            ScopeContainer.Scope scope) {
        container.register(
                new InjectionKey(qualifier, serviceDefinition, false)
                , service, scope, InjectionContainerManager.RegisterType.FINAL
        );
        return this;
    }

    public void printRegistration(PrintStream writer) {
        writer.println("\nThe current results in the container is as follows: ");


        Collection<ServiceRegister> registers = container.getServiceRegister();

        for (ServiceRegister serviceRegister : registers) {

            printRegistration(serviceRegister, writer);
            if (serviceRegister.getOverriddenService() != null) {
                writer.print("-- Overrides service ");
                if (serviceRegister.getOverriddenService().getModule() != null) {
                    writer.println("from module:"
                            + serviceRegister.getOverriddenService().getModule().getClass().getName());
                } else {
                    writer.println("");
                }
                printRegistration(serviceRegister.getOverriddenService(), writer);
            }
            writer.println("\n");
        }

        writer.println("--------- InjectionRegisterModule Information Printer is Done ----------  ");
    }

    private void printRegistration(ServiceRegister serviceRegister, PrintStream writer) {

        if (serviceRegister instanceof ServiceRegisterNamed) {
            ServiceRegisterNamed named = (ServiceRegisterNamed) serviceRegister;
            writer.println("serviceDefinition:     " + named.getKey().getServiceDefinition());
            writer.println("   with qualifier:     " + named.getKey().getQualifier());
            writer.println("   is type safe:       " + (named.getKey().getAnnotation() == null ? "false" : "true"));
            writer.println("serviceImplementation: " + named.getService().getName());
            writer.println("scope:                 " + named.getScope().name());
            writer.println("registrationType:      " + named.getRegisterType().name());

        } else {
            writer.println("serviceImplementation: " + serviceRegister.getService().getName());
            writer.println("scope:                 " + serviceRegister.getScope().name());
            writer.println("registrationType:      " + serviceRegister.getRegisterType().name());
        }
    }

}

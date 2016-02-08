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

import org.hrodberaht.inject.internal.InjectionKey;
import org.hrodberaht.inject.register.InjectionRegister;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class InjectionRegisterJava extends InjectionRegisterBase<InjectionRegister> {

    public InjectionRegisterJava() {
        super();
    }

    public InjectionRegisterJava(Container container) {
        this.container = (SimpleInjection) container;
    }

    public InjectionRegisterJava(InjectionRegister register) {
        super(register);
    }

    public InjectionRegisterJava register(
            String namedService, Class serviceDefinition, Class service, SimpleInjection.Scope scope) {
        container.register(
                new InjectionKey(namedService, serviceDefinition, false)
                , service, scope, SimpleInjection.RegisterType.NORMAL
        );
        return this;
    }

    public InjectionRegisterJava registerDefault(Class serviceDefinition, Class service, SimpleInjection.Scope scope) {
        container.register(serviceDefinition, service, scope, SimpleInjection.RegisterType.WEAK);
        return this;
    }

    public InjectionRegister register(Class serviceDefinition, Class service, SimpleInjection.Scope scope) {
        return super.register(serviceDefinition, service, scope);
    }


    public InjectionRegisterJava register(String namedService, Class serviceDefinition, Class service) {
        register(namedService, serviceDefinition, service, SimpleInjection.Scope.NEW);
        return this;
    }


    public InjectionRegisterJava registerDefault(Class serviceDefinition, Class service) {
        registerDefault(serviceDefinition, service, SimpleInjection.Scope.NEW);
        return this;
    }

    @Override
    public InjectionRegisterJava clone() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        try {
            registerJava.container = this.container.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return registerJava;
    }

}

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
import org.hrodberaht.injection.Module;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.RegistrationModule;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 * <p>
 * <p>
 * Use the InjectionRegisterModule for all variations, will be removed
 * {@link InjectionRegisterModule}
 */
@Deprecated
public class InjectionRegisterJava extends InjectionRegisterBase<InjectionRegister> {

    public InjectionRegisterJava() {
        super();
    }

    public InjectionRegisterJava(InjectContainer container) {
        this.container = (InjectionContainerManager) container;
    }

    public InjectionRegisterJava(InjectionRegister register) {
        super(register);
    }

    public InjectionRegisterJava register(
            String namedService, Class serviceDefinition, Class service, InjectionContainerManager.Scope scope) {
        container.register(
                new InjectionKey(namedService, serviceDefinition, false)
                , service, scope, InjectionContainerManager.RegisterType.NORMAL
        );
        return this;
    }

    public InjectionRegisterJava registerDefault(Class serviceDefinition, Class service, InjectionContainerManager.Scope scope) {
        container.register(serviceDefinition, service, scope, InjectionContainerManager.RegisterType.WEAK);
        return this;
    }

    public InjectionRegister register(Class serviceDefinition, Class service, InjectionContainerManager.Scope scope) {
        return super.register(serviceDefinition, service, scope);
    }


    public InjectionRegisterJava register(String namedService, Class serviceDefinition, Class service) {
        register(namedService, serviceDefinition, service, InjectionContainerManager.Scope.NEW);
        return this;
    }


    public InjectionRegisterJava registerDefault(Class serviceDefinition, Class service) {
        registerDefault(serviceDefinition, service, InjectionContainerManager.Scope.NEW);
        return this;
    }

    @Override
    public InjectionRegister register(RegistrationModule... modules) {
        throw new IllegalAccessError("deprecated use InjectionRegisterModule");
    }

    @Override
    public InjectionRegisterJava copy() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.container = this.container.copy();
        return registerJava;
    }

    @Override
    public Module fillModule(Module module) {
        throw new IllegalAccessError("deprecated use InjectionRegisterModule");
    }

}

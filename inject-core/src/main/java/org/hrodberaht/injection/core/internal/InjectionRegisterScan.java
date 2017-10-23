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

package org.hrodberaht.injection.core.internal;

import org.hrodberaht.injection.core.InjectContainer;
import org.hrodberaht.injection.core.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.core.register.InjectionRegister;
import org.hrodberaht.injection.core.scope.InheritableThreadScope;
import org.hrodberaht.injection.core.scope.ThreadScope;

import javax.inject.Singleton;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 * <p>
 * Use the InjectionRegisterModule for all variations, will be removed
 * {@link InjectionRegisterModule}
 */
@Deprecated
public class InjectionRegisterScan extends InjectionRegisterScanBase<InjectionRegisterScan> {


    public InjectionRegisterScan() {
    }

    public InjectionRegisterScan(InjectionRegister register) {
        super(register);
    }


    @Override
    public InjectContainer getInjectContainer() {
        return container;
    }

    @Override
    public InjectionRegisterScan copy() {
        InjectionRegisterScan registerScan = new InjectionRegisterScan();
        registerScan.container = this.container.copy();
        return registerScan;
    }


    @Override
    public ScopeContainer.Scope getScope(Class serviceClass) {
        if (serviceClass.isAnnotationPresent(Singleton.class)) {
            return ScopeContainer.Scope.SINGLETON;
        } else if (serviceClass.isAnnotationPresent(ThreadScope.class)) {
            return ScopeContainer.Scope.THREAD;
        } else if (serviceClass.isAnnotationPresent(InheritableThreadScope.class)) {
            return ScopeContainer.Scope.INHERITABLE_THREAD;
        }
        return ScopeContainer.Scope.NEW;
    }

    @Override
    public boolean isServiceAnnotated(Class aClazz) {
        return false;
    }

    @Override
    public boolean isInterfaceAnnotated(Class aClazz) {
        return true;
    }
}
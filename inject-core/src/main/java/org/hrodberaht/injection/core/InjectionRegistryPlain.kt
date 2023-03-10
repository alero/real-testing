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

package org.hrodberaht.injection.core;

import org.hrodberaht.injection.core.config.InjectionStoreFactory;
import org.hrodberaht.injection.core.register.InjectionRegister;

/**
 * Created by alexbrob on 2016-03-29.
 */
public class InjectionRegistryPlain implements InjectionRegistry {

    private InjectContainer injectionContainer;
    private InjectionRegister injectionRegister = InjectionStoreFactory.getInjectionRegister();

    public InjectionRegistryPlain register(Module module) {
        injectionRegister.register(module);
        injectionContainer = injectionRegister.getContainer();
        return this;
    }

    public InjectContainer getContainer() {
        return injectionContainer;
    }

    public Module getModule() {
        Module module = new Module(injectionContainer);
        injectionRegister.fillModule(module);
        return module;
    }
}

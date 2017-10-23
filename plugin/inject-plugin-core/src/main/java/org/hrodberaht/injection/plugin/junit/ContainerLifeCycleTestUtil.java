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

package org.hrodberaht.injection.plugin.junit;

import org.hrodberaht.injection.core.internal.InjectionContainerManager;
import org.hrodberaht.injection.core.register.InjectionRegister;
import org.hrodberaht.injection.core.register.RegistrationModule;
import org.hrodberaht.injection.core.register.RegistrationModuleAnnotation;

import javax.inject.Inject;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-13 00:23:43
 * @version 1.0
 * @since 1.0
 * <p/>
 */
public class ContainerLifeCycleTestUtil {

    @Inject
    private InjectionRegister module;


    public void registerServiceInstance(Class serviceDefinition, Object service) {
        RegistrationModuleAnnotation registrationModule = new RegistrationModuleAnnotation() {
            @Override
            public void registrations() {
                register(serviceDefinition)
                        .registerTypeAs(InjectionContainerManager.RegisterType.OVERRIDE_NORMAL)
                        .withInstance(service);
            }
        };
        module.register(registrationModule);
    }

    public void registerServiceInstance(Class serviceDefinition, String qualifier, Object service) {
        RegistrationModuleAnnotation registrationModule = new RegistrationModuleAnnotation() {
            @Override
            public void registrations() {
                register(serviceDefinition)
                        .registerTypeAs(InjectionContainerManager.RegisterType.OVERRIDE_NORMAL)
                        .named(qualifier)
                        .withInstance(service);
            }
        };
        module.register(registrationModule);
    }

    public void registerModule(RegistrationModule module) {
        this.module.register(module);
    }


    public <T> T getService(Class<T> aClass) {
        return module.getContainer().get(aClass);
    }

    public <T> T getService(Class<T> aClass, String name) {
        return module.getContainer().get(aClass, name);
    }
}

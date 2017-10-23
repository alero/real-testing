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

package org.hrodberaht.injection.core.register;

import org.hrodberaht.injection.core.InjectContainer;
import org.hrodberaht.injection.core.Module;
import org.hrodberaht.injection.core.internal.InjectionContainerManager;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * 2010-aug-01 16:43:03
 * @version 1.0
 * @since 1.0
 */
public interface InjectionRegister {

    InjectionRegister register(Class<? extends Annotation> qualifier, Class serviceDefinition, Class service);

    InjectionRegister register(Class serviceDefinition, Class service);

    InjectionRegister register(Class service);

    InjectionRegister overrideRegister(Class<? extends Annotation> qualifier, Class serviceDefinition, Class service);

    InjectionRegister overrideRegister(Class serviceDefinition, Class service);

    InjectionRegister overrideRegister(Class service);

    InjectionRegister finalRegister(Class<? extends Annotation> qualifier, Class serviceDefinition, Class service);

    InjectionRegister finalRegister(Class serviceDefinition, Class service);

    InjectionRegister finalRegister(Class service);

    InjectionContainerManager getInnerContainer();

    InjectContainer getContainer();

    InjectionRegister register(RegistrationModule... modules);

    InjectionRegister copy();

    Module fillModule(Module module);
}

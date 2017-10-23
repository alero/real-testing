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

package org.hrodberaht.injection.internal.annotation;

import org.hrodberaht.injection.internal.InjectionContainerManager;
import org.hrodberaht.injection.internal.InjectionKey;

import javax.inject.Provider;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-maj-29 00:46:09
 * @version 1.0
 * @since 1.0
 */
public class InjectionProvider implements Provider {

    private InjectionKey injectionKey;
    private InjectionContainerManager injection;

    public InjectionProvider(InjectionContainerManager injection, InjectionKey injectionKey) {
        this.injectionKey = injectionKey;
        this.injection = injection;
    }

    @SuppressWarnings(value = "unchecked")
    public Object get() {
        if (injectionKey.getAnnotation() != null) {
            return injection.get(injectionKey.getServiceDefinition(), injectionKey.getAnnotation());
        } else if (injectionKey.getName() != null) {
            return injection.get(injectionKey.getServiceDefinition(), injectionKey.getName());
        }
        return injection.get(injectionKey.getServiceDefinition());


    }
}

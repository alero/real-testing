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

package org.hrodberaht.injection.plugin.junit.resources;

import org.hrodberaht.injection.core.internal.annotation.AnnotationInjection;
import org.hrodberaht.injection.core.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.core.internal.annotation.InjectionPoint;
import org.hrodberaht.injection.core.spi.ContainerConfig;

import java.lang.reflect.Method;
import java.util.List;

public class ChainableInjectionPointProvider implements InjectionFinder {
    private final InjectionFinder injectionFinder;

    public ChainableInjectionPointProvider(InjectionFinder injectionFinder) {
        this.injectionFinder = injectionFinder;
    }

    @Override
    public List<InjectionPoint> findInjectionPoints(Class instanceClass, AnnotationInjection annotationInjection) {
        return injectionFinder.findInjectionPoints(instanceClass, annotationInjection);
    }

    @Override
    public Method findPostConstruct(Class instanceClass) {
        return injectionFinder.findPostConstruct(instanceClass);
    }

    @Override
    public Object extendedInjection(Object instance) {
        return injectionFinder.extendedInjection(instance);
    }

    @Override
    public ContainerConfig getContainerConfig() {
        return injectionFinder.getContainerConfig();
    }
}

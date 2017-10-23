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

package org.hrodberaht.injection.stream;

import org.hrodberaht.injection.spi.ResourceKey;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class ResourcesBuilder {
    private Map<ResourceKey, Object> namedInstances = new HashMap<>();
    private Map<Class, Object> typedInstances = new HashMap<>();

    private Map<ResourceKey, Class> namedType = new HashMap<>();
    private Map<Class, Class> typed = new HashMap<>();

    Map<ResourceKey, Object> getNamedInstances() {
        return namedInstances;
    }

    Map<Class, Object> getTypedInstances() {
        return typedInstances;
    }

    Map<ResourceKey, Class> getNamedType() {
        return namedType;
    }

    Map<Class, Class> getTyped() {
        return typed;
    }

    public ResourcesBuilder resource(String name, Class type, Object instance) {
        namedInstances.put(ResourceKey.of(name, type), instance);
        return this;
    }

    public ResourcesBuilder resource(String name, Object instance) {
        namedInstances.put(ResourceKey.of(name, instance.getClass()), instance);
        return this;
    }

    public ResourcesBuilder resource(Class type, Object instance) {
        typedInstances.put(type, instance);
        return this;
    }

    public ResourcesBuilder resource(Object instance) {
        typedInstances.put(instance.getClass(), instance);
        return this;
    }

    public ResourcesBuilder resource(Class type) {
        typed.put(type, type);
        return this;
    }

    public ResourcesBuilder resource(String name, Class type) {
        namedType.put(ResourceKey.of(name, type), type);
        return this;
    }


}

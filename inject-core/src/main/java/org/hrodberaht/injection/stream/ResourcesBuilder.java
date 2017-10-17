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

    public ResourcesBuilder resource(String name, Class type, Object instance){
        namedInstances.put(ResourceKey.of(name, type), instance);
        return this;
    }

    public ResourcesBuilder resource(String name, Object instance){
        namedInstances.put(ResourceKey.of(name, instance.getClass()), instance);
        return this;
    }

    public ResourcesBuilder resource(Class type, Object instance){
        typedInstances.put(type, instance);
        return this;
    }

    public ResourcesBuilder resource(Object instance){
        typedInstances.put(instance.getClass(), instance);
        return this;
    }

    public ResourcesBuilder resource(Class type){
        typed.put(type, type);
        return this;
    }

    public ResourcesBuilder resource(String name, Class type){
        namedType.put(ResourceKey.of(name, type), type);
        return this;
    }



}

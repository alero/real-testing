package org.hrodberaht.injection.extensions.plugin.junit.resources;

import org.hrodberaht.injection.spi.JavaResourceCreator;
import org.hrodberaht.injection.spi.ResourceFactory;
import org.hrodberaht.injection.spi.ResourceKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluggableResourceFactory implements ResourceFactory {

    private Map<Class, Object> typedMap = new ConcurrentHashMap<>();
    private Map<ResourceKey, Object> namedMap = new ConcurrentHashMap<>();

    public Map<Class, Object> getTypedMap() {
        return typedMap;
    }

    public Map<ResourceKey, Object> getNamedMap() {
        return namedMap;
    }

    @Override
    public <T> JavaResourceCreator<T> getCreator(Class<T> type) {
        // TODO: How do we make it pluggable for the rest of the types?
        return new JavaResourceCreator<T>() {
            @Override
            public T create(String name) {
                ResourceKey key = ResourceKey.of(name, type);
                if(namedMap.get(key) != null){
                    throw new RuntimeException("key value already registered for "+key.toString());
                }
                T instance = getInstance();
                namedMap.put(key, instance);
                return instance;
            }

            private T getInstance() {
                try {
                    return type.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public T create() {
                if(typedMap.get(type) != null){
                    throw new RuntimeException("key value already registered for "+type.getName());
                }
                T instance = getInstance();
                typedMap.put(type, instance);
                return instance;
            }

            @Override
            public T create(String name, T instance) {
                ResourceKey key = ResourceKey.of(name, type);
                if(namedMap.get(key) != null){
                    throw new RuntimeException("instance is already registered for "+key.toString());
                }
                namedMap.put(key, instance);
                return instance;
            }

            @Override
            public T create(T instance) {
                if(typedMap.get(type) != null){
                    throw new RuntimeException("key value already registered for "+type.getName());
                }
                typedMap.put(type, instance);
                return instance;
            }
        };
    }

}

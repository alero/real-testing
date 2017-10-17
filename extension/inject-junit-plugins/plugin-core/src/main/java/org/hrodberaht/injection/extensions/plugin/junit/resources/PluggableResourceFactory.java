package org.hrodberaht.injection.extensions.plugin.junit.resources;

import org.hrodberaht.injection.extensions.plugin.junit.spi.ResourcePlugin;
import org.hrodberaht.injection.spi.JavaResourceCreator;
import org.hrodberaht.injection.spi.ResourceFactory;
import org.hrodberaht.injection.spi.ResourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluggableResourceFactory implements ResourceFactory {


    private static final Logger LOG = LoggerFactory.getLogger(PluggableResourceFactory.class);

    private Map<Class, Object> typedMap = new ConcurrentHashMap<>();
    private Map<ResourceKey, Object> namedMap = new ConcurrentHashMap<>();
    private Map<Class, JavaResourceCreator> customCreator = new ConcurrentHashMap<>();

    public void addCustomCreator(ResourcePlugin resurcePlugin){
        resurcePlugin.getCustomTypes().forEach(aClass -> {
            if(customCreator.get(aClass) != null){
                throw new IllegalArgumentException("Only one custom-resource-creater per type is allowed, found two for type: "+aClass.getName());
            }
            customCreator.put(aClass, resurcePlugin.getCreator(aClass));
        });
    }

    public Map<Class, Object> getTypedMap() {
        return typedMap;
    }

    public Map<ResourceKey, Object> getNamedMap() {
        return namedMap;
    }

    @Override
    public <T> JavaResourceCreator<T> getCreator(Class<T> type) {

        JavaResourceCreator<T> javaResourceCreator = customCreator.get(type);
        if(javaResourceCreator != null){
            LOG.info("using custom Creator {} for type {}", javaResourceCreator.getClass(), type);
            return new JavaResourceCreator<T>() {
                @Override
                public T create(String name) {
                    ResourceKey key = ResourceKey.of(name, type);
                    T instance = javaResourceCreator.create(name);
                    namedMap.put(key, instance);
                    return instance;
                }

                @Override
                public T create() {
                    T instance = javaResourceCreator.create();
                    typedMap.put(type, instance);
                    return instance;
                }

                @Override
                public T create(String name, T instance) {
                    ResourceKey key = ResourceKey.of(name, type);
                    namedMap.put(key, instance);
                    return instance;
                }

                @Override
                public T create(T instance) {
                    typedMap.put(type, instance);
                    return instance;
                }
            };
        }
        LOG.info("using regular Creator for type {}", type);
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

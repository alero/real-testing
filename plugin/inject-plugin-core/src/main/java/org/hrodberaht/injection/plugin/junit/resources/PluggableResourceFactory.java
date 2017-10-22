package org.hrodberaht.injection.plugin.junit.resources;

import org.hrodberaht.injection.plugin.context.ContextManager;
import org.hrodberaht.injection.spi.JavaResourceCreator;
import org.hrodberaht.injection.spi.ResourceFactory;
import org.hrodberaht.injection.spi.ResourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluggableResourceFactory implements ResourceFactory {


    public static void setPluggableResourceFactory(ResourcePluginBase pluginBase, ResourceFactory resourceFactory) {
        pluginBase.resourceFactory = resourceFactory;
    }

    private static final Logger LOG = LoggerFactory.getLogger(PluggableResourceFactory.class);

    private final Map<Class, Object> typedMap = new ConcurrentHashMap<>();
    private final Map<ResourceKey, Object> namedMap = new ConcurrentHashMap<>();
    private final Map<Class, JavaResourceCreator> customCreator = new ConcurrentHashMap<>();
    private final ContextManager contextManager = new ContextManager();


    public Map<Class, Object> getTypedMap() {
        return typedMap;
    }

    public Map<ResourceKey, Object> getNamedMap() {
        return namedMap;
    }

    @Override
    public <T> JavaResourceCreator<T> getCreator(final Class<T> type) {

        JavaResourceCreator<T> javaResourceCreator = customCreator.get(type);
        if (javaResourceCreator != null) {
            LOG.info("using custom Creator {} for type {}", javaResourceCreator.getClass(), type);
            return new JavaResourceCreator<T>() {
                @Override
                public T create(String name) {
                    ResourceKey key = ResourceKey.of(name, type);
                    T instance = javaResourceCreator.create(name);
                    registerInstance(instance, key);
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
                    registerInstance(instance, key);
                    return instance;
                }

                @Override
                public T create(T instance) {
                    typedMap.put(type, instance);
                    return instance;
                }

                @Override
                public Class getType() {
                    return type;
                }
            };
        }
        LOG.info("using regular Creator for type {}", type);
        return new JavaResourceCreator<T>() {
            @Override
            public T create(String name) {
                ResourceKey key = ResourceKey.of(name, type);
                if (namedMap.get(key) != null) {
                    throw new RuntimeException("key value already registered for " + key.toString());
                }
                T instance = getInstance();
                registerInstance(instance, key);
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
                if (typedMap.get(type) != null) {
                    throw new RuntimeException("key value already registered for " + type.getName());
                }
                T instance = getInstance();
                typedMap.put(type, instance);
                return instance;
            }

            @Override
            public T create(String name, T instance) {
                ResourceKey key = ResourceKey.of(name, type);
                if (namedMap.get(key) != null) {
                    throw new RuntimeException("instance is already registered for " + key.toString());
                }
                registerInstance(instance, key);
                return instance;
            }

            @Override
            public T create(T instance) {
                if (typedMap.get(type) != null) {
                    throw new RuntimeException("key value already registered for " + type.getName());
                }
                typedMap.put(type, instance);
                return instance;
            }

            @Override
            public Class getType() {
                return type;
            }
        };
    }

    @Override
    public <T> void addResourceCrator(JavaResourceCreator<T> javaResourceCreator) {
        customCreator.put(javaResourceCreator.getType(), javaResourceCreator);
    }

    private <T> void registerInstance(T instance, ResourceKey key) {
        namedMap.put(key, instance);
        putToContext(key, instance);
    }

    private <T> void putToContext(ResourceKey key, T instance) {
        contextManager.bind(asContextName(key), instance);
    }

    private String asContextName(ResourceKey key) {
        return key.getType().getSimpleName() + "/" + key.getName();
    }

}

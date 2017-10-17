package org.hrodberaht.injection.internal;

import org.hrodberaht.injection.internal.annotation.ReflectionUtils;
import org.hrodberaht.injection.spi.ResourceKey;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alexbrob on 2016-03-31.
 */
public class ResourceInject {




    public void injectResources(Map<Class, Object> typedResources, Map<ResourceKey, Object> namedResources, Object serviceInstance) {

        List<Member> members = ReflectionUtils.findMembers(serviceInstance.getClass());
        for (Member member : members) {
            if (member instanceof Field) {
                Field field = (Field) member;
                if (field.isAnnotationPresent(Resource.class)) {
                    Resource resource = field.getAnnotation(Resource.class);
                    if (!injectNamedResource(namedResources, serviceInstance, field, resource)) {
                        injectTypedResource(typedResources, serviceInstance, field);
                    }
                }
            }
        }
    }

    private boolean injectTypedResource(Map<Class, Object> typedResources, Object serviceInstance, Field field) {
        if (typedResources == null) {
            return false;
        }
        Object value = typedResources.get(field.getType());
        if (value != null) {
            injectResourceValue(serviceInstance, field, value);
            return true;
        }
        return false;
    }

    private boolean injectNamedResource(Map<ResourceKey, Object> namedResources, Object serviceInstance, Field field, Resource resource) {
        if (namedResources == null) {
            return false;
        }
        if("".equals(resource.name())){
            return false;
        }
        ResourceKey key = ResourceKey.of(resource.name(),field.getType());
        Object value = namedResources.get(key);
        if (value == null) {
            ResourceKey mappedKey = ResourceKey.of(resource.mappedName(),field.getType());
            value = namedResources.get(mappedKey);
        }
        if (value != null) {
            injectResourceValue(serviceInstance, field, value);
            return true;
        }
        return false;
    }

    private void injectResourceValue(Object serviceInstance, Field field, Object value) {
        if (value != null) {
            boolean accessible = field.isAccessible();
            try {
                if (!accessible) {
                    field.setAccessible(true);
                }
                field.set(serviceInstance, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                if (!accessible) {
                    field.setAccessible(false);
                }
            }
        }
    }

}

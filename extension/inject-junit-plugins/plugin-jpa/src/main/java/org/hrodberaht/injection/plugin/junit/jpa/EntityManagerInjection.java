package org.hrodberaht.injection.plugin.junit.jpa;

import org.hrodberaht.injection.internal.annotation.ReflectionUtils;

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
public class EntityManagerInjection {

    private boolean hasPersistenceContextInClassPath = true;
    private Map<String, EntityManager> entityManagers = null;


    public void injectResources(Object serviceInstance) {

        if (entityManagers == null) {
            return;
        }

        List<Member> members = ReflectionUtils.findMembers(serviceInstance.getClass());
        for (Member member : members) {
            if (member instanceof Field) {
                Field field = (Field) member;
                if (hasPersistenceContextInClassPath) {
                    try {
                        if (field.isAnnotationPresent(PersistenceContext.class)) {
                            PersistenceContext resource = field.getAnnotation(PersistenceContext.class);
                            injectEntityManager(serviceInstance, field, resource);
                        }
                    } catch (NoClassDefFoundError e) {
                        hasPersistenceContextInClassPath = false;
                    }
                }
            }
        }
    }

    public EntityManager addPersistenceContext(String name, EntityManager entityManager) {
        if (entityManagers == null) {
            entityManagers = new HashMap<>();
        }
        entityManagers.put(name, entityManager);
        return entityManager;
    }

    private void injectEntityManager(Object serviceInstance, Field field, PersistenceContext resource) {
        if(entityManagers == null){
            throw new IllegalAccessError("Entity manager not registered");
        }
        Object value = entityManagers.get(resource.unitName());
        if (value == null && entityManagers.size() == 1 && "".equals(resource.unitName())) {
            value = entityManagers.values().iterator().next();
        }
        injectResourceValue(serviceInstance, field, value);
    }


    protected void injectResourceValue(Object serviceInstance, Field field, Object value) {
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

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

package org.hrodberaht.injection.internal;

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
public class ResourceInjection {

    private Map<String, Object> resources = null;
    private Map<Class, Object> typedResources = null;
    private boolean hasPersistenceContextInClassPath = true;
    private Map<String, EntityManager> entityManagers = null;
    private Map<Class, Object> typeResources;

    public void injectResources(Object serviceInstance) {

        if (resources == null && entityManagers == null && typedResources == null) {
            return;
        }

        List<Member> members = ReflectionUtils.findMembers(serviceInstance.getClass());
        for (Member member : members) {
            if (member instanceof Field) {
                Field field = (Field) member;
                if (field.isAnnotationPresent(Resource.class)) {
                    Resource resource = field.getAnnotation(Resource.class);
                    if (!injectNamedResource(serviceInstance, field, resource)) {
                        injectTypedResource(serviceInstance, field);
                    }
                }
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

    public void addResource(String name, Object value) {
        if (resources == null) {
            resources = new HashMap<>();
        }
        resources.put(name, value);
    }

    public void addResource(Class typedName, Object value) {
        if (typedResources == null) {
            typedResources = new HashMap<>();
        }
        typedResources.put(typedName, value);
    }

    public Collection<EntityManager> getEntityManagers() {
        if (entityManagers == null) {
            return null;
        }
        return entityManagers.values();
    }

    public EntityManager getEntityManager(String name) {
        if (entityManagers == null) {
            return null;
        }
        return entityManagers.get(name);
    }

    public void addPersistenceContext(String name, EntityManager entityManager) {
        if (entityManagers == null) {
            entityManagers = new HashMap<>();
        }
        entityManagers.put(name, entityManager);
    }

    private void injectEntityManager(Object serviceInstance, Field field, PersistenceContext resource) {
        if (entityManagers == null) {
            throw new IllegalAccessError("Entity manager not registered");
        }
        Object value = entityManagers.get(resource.unitName());
        if (value == null && entityManagers.size() == 1 && "".equals(resource.unitName())) {
            value = entityManagers.values().iterator().next();
        }
        injectResourceValue(serviceInstance, field, value);
    }

    protected boolean injectTypedResource(Object serviceInstance, Field field) {
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

    protected boolean injectNamedResource(Object serviceInstance, Field field, Resource resource) {
        if (resources == null) {
            return false;
        }
        Object value = resources.get(resource.name());
        if (value == null) {
            value = resources.get(resource.mappedName());
        }
        if (value != null) {
            injectResourceValue(serviceInstance, field, value);
            return true;
        }
        return false;
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

    public Map<Class, Object> getTypeResources() {
        return typeResources;
    }
}

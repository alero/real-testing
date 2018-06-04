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

package org.hrodberaht.injection.plugin.junit.jpa;

import org.hrodberaht.injection.core.internal.annotation.ReflectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityManagerInjection {

    private static Map<Class, List<Field>> foundMembersMap = new ConcurrentHashMap<>();
    private boolean hasPersistenceContextInClassPath = true;
    private Map<String, EntityManager> entityManagers = null;

    public void injectResources(Object serviceInstance) {

        if (entityManagers == null) {
            return;
        }

        List<Field> foundFields = foundMembersMap.computeIfAbsent(serviceInstance.getClass(), aClass -> {
            List<Field> foundFieldsInner = new ArrayList<>();
            List<Member> members = ReflectionUtils.findMembers(serviceInstance.getClass());
            for (Member member : members) {
                if (member instanceof Field) {
                    Field field = (Field) member;
                    if (hasPersistenceContextInClassPath) {
                        try {
                            if (field.isAnnotationPresent(PersistenceContext.class)) {
                                foundFieldsInner.add(field);
                            }
                        } catch (NoClassDefFoundError e) {
                            hasPersistenceContextInClassPath = false;
                        }
                    }
                }
            }
            return foundFieldsInner;
        });

        foundFields.forEach(field -> {
            PersistenceContext resource = field.getAnnotation(PersistenceContext.class);
            injectEntityManager(serviceInstance, field, resource);
        });
    }

    public EntityManager addPersistenceContext(String name, EntityManager entityManager) {
        if (entityManagers == null) {
            entityManagers = new HashMap<>();
        }
        entityManagers.put(name, entityManager);
        return entityManager;
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

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

package org.hrodberaht.injection.core.internal;

import org.hrodberaht.injection.core.internal.annotation.ReflectionUtils;
import org.hrodberaht.injection.core.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.core.spi.ResourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceInject {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceInject.class);

    private static final Map<Class, ResourceFields> foundTypedFields = new ConcurrentHashMap<>();

    public void injectResources(Map<Class, Object> typedResources, Map<ResourceKey, Object> namedResources, Object serviceInstance) {

        ResourceFields foundFields = foundTypedFields.computeIfAbsent(serviceInstance.getClass(), aClass -> {
            ResourceFields resourceFields = new ResourceFields();
            List<Member> members = ReflectionUtils.findMembers(serviceInstance.getClass());
            for (Member member : members) {
                if (member instanceof Field) {
                    Field field = (Field) member;
                    if (field.isAnnotationPresent(Resource.class)) {
                        Resource resource = field.getAnnotation(Resource.class);
                        if (hasNameOrMappedName(resource)) {
                            resourceFields.namedFields.add(new ResourceValue(resource, field));
                        } else {
                            resourceFields.typedFields.add(field);
                        }
                    }
                }
            }
            return resourceFields;
        });

        foundFields.typedFields.forEach(field -> {
            injectTypedResource(typedResources, serviceInstance, field);
        });

        foundFields.namedFields.forEach(resourceValue -> {
            if (!injectNamedResource(namedResources, serviceInstance, resourceValue.field, resourceValue.resource)) {
                throw new InjectRuntimeException("No resource found for " + descriptive(resourceValue.field, resourceValue.resource));
            }
        });
    }

    private class ResourceFields{
        List<ResourceValue> namedFields = new ArrayList<>();
        List<Field> typedFields = new ArrayList<>();
    }

    private class ResourceValue{
        private final Resource resource;
        private final Field field;

        private ResourceValue(Resource resource, Field field) {
            this.resource = resource;
            this.field = field;
        }
    }

    private String descriptive(Field field, Resource resource) {
        return hasName(resource) ?
                getDescriptiveName(field, resource) :
                getDescriptiveMappedName(field, resource);
    }

    private String getDescriptiveName(Field field, Resource resource) {
        return "field:'" + field.getName() + "' name:'" + resource.name() + "'";
    }

    private String getDescriptiveMappedName(Field field, Resource resource) {
        return hasMappedName(resource) ? "field:'" + field.getName() + "' mapped-name:'" + resource.mappedName() + "'" : "no name?";
    }


    private boolean hasNameOrMappedName(Resource resource) {
        return hasName(resource) || hasMappedName(resource);
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
        ResourceKey key = ResourceKey.of(resource.name(), field.getType());
        Object value = namedResources.get(key);
        if (value == null) {
            ResourceKey mappedKey = ResourceKey.of(resource.mappedName(), field.getType());
            value = namedResources.get(mappedKey);
        } else if (hasMappedName(resource)) {
            LOG.debug("using name to inject, mapped name exists though");
        }
        if (value != null) {
            injectResourceValue(serviceInstance, field, value);
            return true;
        }
        return false;
    }

    private boolean hasName(Resource resource) {
        return !"".equals(resource.name());
    }

    private boolean hasMappedName(Resource resource) {
        return !"".equals(resource.mappedName());
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

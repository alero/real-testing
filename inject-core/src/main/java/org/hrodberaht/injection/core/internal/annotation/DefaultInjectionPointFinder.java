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

package org.hrodberaht.injection.core.internal.annotation;

import org.hrodberaht.injection.core.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.core.spi.ContainerConfig;
import org.hrodberaht.injection.core.spi.ContainerConfigBuilder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DefaultInjectionPointFinder implements InjectionFinder {

    private ContainerConfig containerConfig;
    private ContainerConfigBuilder containerConfigBuilder;

    public DefaultInjectionPointFinder() {
    }

    public DefaultInjectionPointFinder(ContainerConfig containerConfig) {
        this.containerConfig = containerConfig;
    }

    public DefaultInjectionPointFinder(ContainerConfigBuilder containerConfigBuilder) {
        this.containerConfigBuilder = containerConfigBuilder;
    }

    /**
     * Finds all injection points for a class analysing fields and methods
     *
     * @param service the class to analyze
     * @return found injection points
     */
    public List<InjectionPoint> findInjectionPoints(Class service, AnnotationInjection annotationInjection) {
        List<InjectionPoint> injectionPoints = new ArrayList<InjectionPoint>();
        if (service == null) {
            return injectionPoints;
        }
        List<Method> allMethods = ReflectionUtils.findMethods(service);
        List<Member> allMembers = ReflectionUtils.findMembers(service);
        for (Member member : allMembers) {
            if (member instanceof Field) {
                Field field = (Field) member;
                if (fieldNeedsInjection(field)) {
                    injectionPoints.add(new InjectionPoint(field, annotationInjection));
                }
            } else if (member instanceof Method) {
                Method method = (Method) member;
                if (methodNeedsInjection(method) &&
                        // This makes sure that overridden methods are not injected
                        !ReflectionUtils.isOverridden(method, allMethods)) {
                    injectionPoints.add(new InjectionPoint(method, annotationInjection));
                }
            } else {
                throw new UnsupportedOperationException("Unsupported member: " + member);
            }
        }
        return injectionPoints;
    }

    public Method findPostConstruct(Class serviceClass) {
        List<Method> allMethods = ReflectionUtils.findMethods(serviceClass);
        Method foundMethod = null;
        for (Method method : allMethods) {
            if (methodHasPostConstruct(method)) {
                if (foundMethod != null) {
                    throw new InjectRuntimeException("Several PostConstruct annotations found, make sure there is only one");
                }
                foundMethod = method;
            }
        }
        if (foundMethod != null && !foundMethod.isAccessible()) {
            foundMethod.setAccessible(true);
        }
        return foundMethod;
    }

    public Object extendedInjection(Object service) {
        ContainerConfigBuilder builder = getContainerConfigBuilder();
        if (builder != null) {
            builder.injectResources(service);
        }
        return service;
    }

    public ContainerConfig getContainerConfig() {
        return containerConfig;
    }

    public ContainerConfigBuilder getContainerConfigBuilder() {
        return containerConfigBuilder;
    }

    /**
     * Intended for override to support other annotations
     *
     * @param method the method
     * @return if the method contains PostConstruct annotation
     */
    protected boolean hasPostConstructAnnotation(Method method) {
        return method.isAnnotationPresent(PostConstruct.class);
    }

    /**
     * Intended for override to support other annotations
     *
     * @param method the method
     * @return if the method should be injected
     */
    protected boolean hasInjectAnnotationOnMethod(Method method) {
        return method.isAnnotationPresent(Inject.class);
    }

    /**
     * Intended for override to support other annotations
     *
     * @param field the field
     * @return if the field should be injected
     */
    @SuppressWarnings("squid:S2109")
    protected boolean hasInjectAnnotationOnField(Field field) {
        return field.isAnnotationPresent(Inject.class);
    }

    private boolean methodHasPostConstruct(Method method) {
        return !ReflectionUtils.isStatic(method)
                && hasPostConstructAnnotation(method);
    }

    private boolean methodNeedsInjection(Method method) {
        return !ReflectionUtils.isStatic(method)
                && hasInjectAnnotationOnMethod(method);
    }

    private boolean fieldNeedsInjection(Field field) {
        return !ReflectionUtils.isStatic(field)
                && !ReflectionUtils.isFinal(field)
                && hasInjectAnnotationOnField(field);
    }


}

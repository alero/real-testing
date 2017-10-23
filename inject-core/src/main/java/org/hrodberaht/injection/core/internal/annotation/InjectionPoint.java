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

import org.hrodberaht.injection.core.internal.InjectionKey;
import org.hrodberaht.injection.core.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.core.internal.stats.Statistics;
import org.hrodberaht.injection.core.register.VariableInjectionFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-maj-29 00:52:31
 * @version 1.0
 * @since 1.0
 */
public class InjectionPoint {
    public enum InjectionPointType {METHOD, FIELD}

    private List<InjectionMetaData> dependencies;
    private InjectionPointType type;
    private Field field;
    private Method method;
    private Boolean accessible = null;

    public InjectionPoint(Field field) {
        type = InjectionPointType.FIELD;
        this.field = field;
    }

    public InjectionPoint(Method method) {
        type = InjectionPointType.METHOD;
        this.method = method;
    }

    public InjectionPoint(Field field, AnnotationInjection annotationInjection) {
        type = InjectionPointType.FIELD;
        this.field = field;
        dependencies = new ArrayList<>(1);
        dependencies.add(findDependency(field, annotationInjection));
    }

    public InjectionPoint(Method method, AnnotationInjection annotationInjection) {
        type = InjectionPointType.METHOD;
        this.method = method;
        dependencies = findDependencies(method, annotationInjection);
    }

    public InjectionPointType getType() {
        return type;
    }

    public List<InjectionMetaData> getDependencies() {
        return dependencies;
    }

    public void injectField(Object service, Object serviceDependencies) {
        invokeField(service, serviceDependencies);
    }

    public void injectMethod(Object service, Object... serviceDependencies) {
        invokeMethod(service, serviceDependencies);
    }

    private void invokeMethod(Object service, Object... serviceDependency) {
        if (accessible == null) {
            final boolean originalAccessible = method.isAccessible();
            if (!originalAccessible) {
                method.setAccessible(true);
            }
            accessible = true;
        }

        try {
            method.invoke(service, serviceDependency);
            Statistics.addInjectMethodCount();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } finally {
            // Do not reset the accessible as multi-thread calls
            // will have problems with singletons and this way of doing it 
            // method.setAccessible(originalAccessible);
        }
    }

    private void invokeField(Object service, Object serviceDependency) {
        if (accessible == null) {
            final boolean originalAccessible = field.isAccessible();
            if (!originalAccessible) {
                field.setAccessible(true);
            }
            accessible = true;
        }
        try {
            field.set(service, serviceDependency);
            Statistics.addInjectFieldCount();
        } catch (final IllegalAccessException e) {
            throw new InjectRuntimeException(e);
        } finally {
            // Do not reset the accessible as multi-thread calls
            // will have serious access problems (it will be closed by this call when the next one comes)
            // field.setAccessible(originalAccessible);
        }
    }

    private List<InjectionMetaData> findDependencies(final Method method, AnnotationInjection annotationInjection) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        return InjectionUtils.findDependencies(
                parameterTypes, genericParameterTypes, parameterAnnotations, annotationInjection
        );
    }

    private InjectionMetaData findDependency(final Field field, AnnotationInjection annotationInjection) {
        Class fieldBeanClass = field.getType();


        if (InjectionUtils.isProvider(fieldBeanClass)) {
            final Type genericType = field.getGenericType();
            final Class beanClassFromProvider = InjectionUtils.getClassFromProvider(field, genericType);
            InjectionKey key = AnnotationQualifierUtil.getQualifierKey(beanClassFromProvider, field.getAnnotations(), true);
            if (key != null) {
                Class serviceImpl = annotationInjection.findServiceClassAndRegister(InjectionKey.purify(key));
                return annotationInjection.findInjectionData(serviceImpl, key);
            } else {
                if (InjectionUtils.isVariableProvider(fieldBeanClass)) {
                    key = new InjectionKey(VariableInjectionFactory.SERVICE_NAME, beanClassFromProvider, true);
                } else {
                    key = new InjectionKey(beanClassFromProvider, true);
                }
                return annotationInjection.findInjectionData(beanClassFromProvider, key);
            }
        } else {
            InjectionKey qualifier = AnnotationQualifierUtil.getQualifierKey(fieldBeanClass, field.getAnnotations(), false);
            if (qualifier == null) {
                qualifier = new InjectionKey(fieldBeanClass, false);
            }
            Class serviceClass = annotationInjection.findServiceClassAndRegister(qualifier);
            return annotationInjection.findInjectionData(serviceClass, qualifier);
        }
    }
}

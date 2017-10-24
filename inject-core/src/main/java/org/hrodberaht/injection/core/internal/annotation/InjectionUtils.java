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

import org.hrodberaht.injection.core.annotation.VariableProvider;
import org.hrodberaht.injection.core.internal.InjectionKey;
import org.hrodberaht.injection.core.internal.ScopeContainer;
import org.hrodberaht.injection.core.internal.annotation.scope.DefaultScopeHandler;
import org.hrodberaht.injection.core.internal.annotation.scope.InheritableThreadScopeHandler;
import org.hrodberaht.injection.core.internal.annotation.scope.ScopeHandler;
import org.hrodberaht.injection.core.internal.annotation.scope.SingletonScopeHandler;
import org.hrodberaht.injection.core.internal.annotation.scope.ThreadScopeHandler;
import org.hrodberaht.injection.core.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.core.scope.InheritableThreadScope;
import org.hrodberaht.injection.core.scope.ThreadScope;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Scope;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class InjectionUtils {
    private InjectionUtils() {
    }

    static Class<Object> getClassFromProvider(final Object serviceClass, final Type serviceType) {
        if (serviceType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) serviceType;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            return (Class<Object>) typeArguments[0];
        }
        throw new IllegalArgumentException("Provider used without generic argument: " + serviceClass);
    }

    static List<InjectionMetaData> findDependencies(
            Class[] parameterTypes, Type[] genericParameterType, Annotation[][] parameterAnnotation
            , AnnotationInjection annotationInjection
    ) {
        List<InjectionMetaData> injectionMetaData = new ArrayList<>(parameterTypes.length);

        for (int i = 0; i < parameterTypes.length; i++) {
            Class serviceDefinition = parameterTypes[i];
            Annotation[] annotations = parameterAnnotation[i];
            if (InjectionUtils.isProvider(serviceDefinition)) {
                Type serviceType = genericParameterType[i];
                Class<Object> beanClassFromProvider =
                        InjectionUtils.getClassFromProvider(serviceDefinition, serviceType);
                addInjectMetaData(injectionMetaData, beanClassFromProvider, annotations, annotationInjection, true);
            } else {
                addInjectMetaData(injectionMetaData, serviceDefinition, annotations, annotationInjection, false);
            }


        }
        return injectionMetaData;
    }

    private static void addInjectMetaData(
            List<InjectionMetaData> injectionMetaData,
            Class serviceDefinition,
            Annotation[] annotations,
            AnnotationInjection annotationInjection,
            boolean provider) {
        InjectionKey key = AnnotationQualifierUtil.getQualifierKey(serviceDefinition, annotations, provider);
        Class serviceImplClass;
        if (key != null) {
            if (!provider) {
                serviceImplClass = annotationInjection.findServiceClassAndRegister(key);
            } else {
                serviceImplClass = annotationInjection.findServiceClassAndRegister(InjectionKey.purify(key));
            }
        } else {
            key = new InjectionKey(serviceDefinition, provider);
            serviceImplClass = annotationInjection.findServiceClassAndRegister(key);
        }
        injectionMetaData.add(
                annotationInjection.findInjectionData(
                        serviceImplClass,
                        key
                )
        );
    }

    static Constructor findConstructor(final Class<Object> beanClass) {
        try {
            Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();

            List<Constructor<?>> annotatedConstructors = new ArrayList<Constructor<?>>();

            for (Constructor<?> constructor : declaredConstructors) {
                if (InjectionUtils.constructorNeedsInjection(constructor)) {
                    annotatedConstructors.add(constructor);
                }
            }

            if (annotatedConstructors.isEmpty()) {
                return getConstructorOrNull(beanClass);
            } else if (annotatedConstructors.size() > 1) {
                throw new InjectRuntimeException(
                        "Several annotated constructors found for autowire {0} {1}", beanClass, annotatedConstructors);
            }

            return annotatedConstructors.get(0);
        } catch (Exception throwable) {
            throw new InjectRuntimeException(throwable);
        }
    }

    private static Constructor getConstructorOrNull(Class<Object> beanClass) {
        try {
            return beanClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static boolean constructorNeedsInjection(final Constructor<?> constructor) {
        return constructor.isAnnotationPresent(Inject.class);
    }


    static boolean isProvider(Class service) {
        return Provider.class.isAssignableFrom(service) || VariableProvider.class.isAssignableFrom(service);
    }

    static boolean isVariableProvider(Class service) {
        return VariableProvider.class.isAssignableFrom(service);
    }

    private static boolean isSingleton(Class beanClass) {
        Annotation scope = getScope(beanClass);
        return scope instanceof Singleton;
    }

    private static boolean isThread(Class beanClass) {
        Annotation scope = getScope(beanClass);
        return scope instanceof ThreadScope;
    }

    private static boolean isInheritedThread(Class beanClass) {
        Annotation scope = getScope(beanClass);
        if (scope instanceof InheritableThreadScope) {
            return true;
        }
        return false;
    }

    private static Annotation getScope(final Class<?> beanClass) {
        List<Annotation> scopeAnnotations = new ArrayList<Annotation>();
        Annotation[] annotations = beanClass.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(Scope.class)) {
                scopeAnnotations.add(annotation);
            }
        }

        if (scopeAnnotations.isEmpty()) {
            return null;
        } else if (scopeAnnotations.size() > 1) {
            throw new InjectRuntimeException(
                    "More than one scope annotations found on {0} {1}", beanClass, scopeAnnotations);
        }

        return scopeAnnotations.get(0);
    }

    static ScopeHandler getScopeHandler(Class serviceClass) {
        return getScopeHandler(serviceClass, null);
    }

    static ScopeHandler getScopeHandler(Class serviceClass, ScopeContainer.Scope scope) {

        if (scope != null) { // first follow the enforced scope control, do not look for annotations
            if (scope == ScopeContainer.Scope.SINGLETON) {
                return new SingletonScopeHandler();
            } else if (scope == ScopeContainer.Scope.THREAD) {
                return new ThreadScopeHandler();
            } else if (scope == ScopeContainer.Scope.INHERITABLE_THREAD) {
                return new InheritableThreadScopeHandler();
            }
        } else {
            if (isSingleton(serviceClass)) {
                return new SingletonScopeHandler();
            } else if (isThread(serviceClass)) {
                return new ThreadScopeHandler();
            } else if (isInheritedThread(serviceClass)) {
                return new InheritableThreadScopeHandler();
            }
        }
        return new DefaultScopeHandler();
    }


}

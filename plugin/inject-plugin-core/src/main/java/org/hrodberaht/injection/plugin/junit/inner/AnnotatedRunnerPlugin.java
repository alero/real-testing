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

package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.core.internal.annotation.ReflectionUtils;
import org.hrodberaht.injection.core.register.InjectionRegister;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterClassTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterContainerCreation;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeClassTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeContainerCreation;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotatedRunnerPlugin {
    private static final Logger LOG = LoggerFactory.getLogger(AnnotatedRunnerPlugin.class);
    private static Map<AnnotationKey, List<Method>> annotationKeyMethodsMap = new ConcurrentHashMap<>();

    private static Set<Class> supporedAnnotations = new HashSet<>(Arrays.asList(
            RunnerPluginBeforeContainerCreation.class,
            RunnerPluginAfterContainerCreation.class,
            RunnerPluginBeforeClassTest.class,
            RunnerPluginAfterClassTest.class,
            RunnerPluginBeforeTest.class,
            RunnerPluginAfterTest.class
    ));

    private final Map<Class, Plugin> annotatedPlugin = new ConcurrentHashMap<>();

    public static boolean containsRunnerAnnotations(Plugin plugin) {
        return AnnotatedRunnerBase.containsRunnerAnnotations(plugin, supporedAnnotations);
    }


    Plugin addPlugin(Plugin plugin) {
        if (annotatedPlugin.get(plugin.getClass()) != null) {
            LOG.info("reused plugin " + plugin.getClass());
            return annotatedPlugin.get(plugin.getClass());
        }
        LOG.info("added plugin " + plugin.getClass());
        annotatedPlugin.put(plugin.getClass(), plugin);
        return plugin;
    }


    void findAnnotationAndInvokeMethod(PluginRunnerBase runnerBase, InjectionRegister injectionRegister, Class<Annotation> annotation) {
        annotatedPlugin.forEach((aClass, plugin) -> runnerBase.runIfActive(aClass, () -> {
            AnnotationKey annotationKey = new AnnotationKey(aClass, annotation);
            List<Method> foundMethods = annotationKeyMethodsMap.computeIfAbsent(annotationKey, annotationKey1 -> {
                List<Method> foundMethodsInner = new ArrayList<>();
                for (Method method : ReflectionUtils.findMethods(aClass)) {
                    if (method.getAnnotation(annotation) != null) {
                        if (!method.isAccessible()) {
                            method.setAccessible(true);
                        }
                        foundMethodsInner.add(method);
                    }
                }
                return foundMethodsInner;
            });


            foundMethods.forEach(method -> {
                try {
                    if (injectionRegister != null && method.getParameterCount() == 1 && method.getParameterTypes()[0].isAssignableFrom(InjectionRegister.class)) {
                        method.invoke(plugin, injectionRegister);
                    } else {
                        method.invoke(plugin);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        }));

    }

    private class AnnotationKey {
        private final Class pluginClass;
        private final Class<Annotation> annotationClass;

        private AnnotationKey(Class pluginClass, Class<Annotation> annotationClass) {
            this.pluginClass = pluginClass;
            this.annotationClass = annotationClass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AnnotationKey that = (AnnotationKey) o;

            if (pluginClass != null ? !pluginClass.equals(that.pluginClass) : that.pluginClass != null) return false;
            return annotationClass != null ? annotationClass.equals(that.annotationClass) : that.annotationClass == null;
        }

        @Override
        public int hashCode() {
            int result = pluginClass != null ? pluginClass.hashCode() : 0;
            result = 31 * result + (annotationClass != null ? annotationClass.hashCode() : 0);
            return result;
        }
    }
}

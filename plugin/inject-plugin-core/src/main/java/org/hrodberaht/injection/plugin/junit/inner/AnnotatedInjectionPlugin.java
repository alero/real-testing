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

import org.hrodberaht.injection.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.internal.annotation.ReflectionUtils;
import org.hrodberaht.injection.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.plugin.junit.spi.InjectionPlugin;
import org.hrodberaht.injection.plugin.junit.spi.Plugin;
import org.hrodberaht.injection.plugin.junit.spi.annotation.InjectionPluginInjectionFinder;
import org.hrodberaht.injection.plugin.junit.spi.annotation.InjectionPluginInjectionRegister;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.spi.ContainerConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotatedInjectionPlugin {
    private static final Logger LOG = LoggerFactory.getLogger(AnnotatedInjectionPlugin.class);
    private static Map<Class, Method> classInjectionFinderMethodMap = new ConcurrentHashMap<>();
    private static Map<Class, Method> classInjectionRegisterMethodMap = new ConcurrentHashMap<>();

    private static Set<Class> supporedAnnotations = new HashSet<>(Arrays.asList(
            InjectionPluginInjectionFinder.class,
            InjectionPluginInjectionRegister.class
    ));

    public static boolean containsAnnotations(Plugin plugin) {
        return AnnotatedRunnerBase.containsRunnerAnnotations(plugin, supporedAnnotations);
    }


    public static <T extends Plugin> InjectionPlugin createPluginWrapper(final T plugin) {
        Method injectionFinderMethod = findInjectionFinderMethod(plugin);
        Method injectionRegisterMethod = findInjectionRegisterMethod(plugin);
        return new InjectionPlugin() {
            @Override
            public void setInjectionRegister(InjectionRegister injectionRegister) {
                if (injectionRegisterMethod == null) {
                    return;
                }
                try {
                    injectionRegisterMethod.invoke(plugin, injectionRegister);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new InjectRuntimeException(e);
                }
            }

            @Override
            public InjectionFinder getInjectionFinder(ContainerConfigBuilder containerConfigBuilder) {
                if (injectionFinderMethod == null) {
                    return null;
                }
                try {
                    return (InjectionFinder) injectionFinderMethod.invoke(plugin, containerConfigBuilder);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new InjectRuntimeException(e);
                }
            }
        };
    }

    private static <T extends Plugin> Method findInjectionRegisterMethod(T plugin) {
        return classInjectionRegisterMethodMap.computeIfAbsent(plugin.getClass(), aClass -> {
            for (Method method : ReflectionUtils.findMethods(plugin.getClass())) {
                if (method.getAnnotation(InjectionPluginInjectionRegister.class) != null) {
                    LOG.info("found InjectionPluginInjectionRegister at {} in {}", method.getName(), aClass.getName());
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    if (method.getParameterCount() != 1 || !method.getParameterTypes()[0].isAssignableFrom(InjectionRegister.class)) {
                        throw new InjectRuntimeException("method with InjectionPluginInjectionFinder must have a parameter of type ContainerConfigBuilder");
                    } else {
                        return method;
                    }
                }
            }
            return null;
        });
    }

    private static <T extends Plugin> Method findInjectionFinderMethod(T plugin) {

        return classInjectionFinderMethodMap.computeIfAbsent(plugin.getClass(), aClass -> {
            for (Method method : ReflectionUtils.findMethods(plugin.getClass())) {
                if (method.getAnnotation(InjectionPluginInjectionFinder.class) != null) {
                    LOG.info("found InjectionPluginInjectionFinder at {} in {}", method.getName(), aClass.getName());
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    if (!InjectionFinder.class.isAssignableFrom(method.getReturnType())) {
                        throw new InjectRuntimeException("method with InjectionPluginInjectionFinder must have a returnType of InjectionFinder");
                    } else if (method.getParameterCount() != 1 || !method.getParameterTypes()[0].isAssignableFrom(ContainerConfigBuilder.class)) {
                        throw new InjectRuntimeException("method with InjectionPluginInjectionFinder must have a parameter of type ContainerConfigBuilder");
                    } else {
                        return method;
                    }
                }
            }
            return null;
        });
    }
}

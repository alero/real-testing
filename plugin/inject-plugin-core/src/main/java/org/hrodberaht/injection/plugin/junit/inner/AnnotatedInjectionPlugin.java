package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.internal.annotation.ReflectionUtils;
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
import java.util.Set;

public class AnnotatedInjectionPlugin {
    private static final Logger LOG = LoggerFactory.getLogger(AnnotatedInjectionPlugin.class);


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
                    throw new RuntimeException(e);
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
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private static <T extends Plugin> Method findInjectionRegisterMethod(T plugin) {
        for (Method method : ReflectionUtils.findMethods(plugin.getClass())) {
            if (method.getAnnotation(InjectionPluginInjectionRegister.class) != null) {
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                if (method.getParameterCount() != 1 || !method.getParameterTypes()[0].isAssignableFrom(InjectionRegister.class)) {
                    throw new RuntimeException("method with InjectionPluginInjectionFinder must have a parameter of type ContainerConfigBuilder");
                } else {
                    return method;
                }
            }
        }
        return null;
    }

    private static <T extends Plugin> Method findInjectionFinderMethod(T plugin) {
        for (Method method : ReflectionUtils.findMethods(plugin.getClass())) {
            if (method.getAnnotation(InjectionPluginInjectionFinder.class) != null) {

                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                if (!InjectionFinder.class.isAssignableFrom(method.getReturnType())) {
                    throw new RuntimeException("method with InjectionPluginInjectionFinder must have a returnType of InjectionFinder");
                } else if (method.getParameterCount() != 1 || !method.getParameterTypes()[0].isAssignableFrom(ContainerConfigBuilder.class)) {
                    throw new RuntimeException("method with InjectionPluginInjectionFinder must have a parameter of type ContainerConfigBuilder");
                } else {
                    return method;
                }
            }
        }
        return null;
    }
}

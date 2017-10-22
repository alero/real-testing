package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.internal.annotation.ReflectionUtils;
import org.hrodberaht.injection.plugin.junit.resources.ChainableInjectionPointProvider;
import org.hrodberaht.injection.plugin.junit.spi.Plugin;
import org.hrodberaht.injection.plugin.junit.spi.annotation.ResourcePluginChainableInjectionProvider;
import org.hrodberaht.injection.plugin.junit.spi.annotation.ResourcePluginFactory;
import org.hrodberaht.injection.spi.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AnnotatedResourcePlugin {
    private static final Logger LOG = LoggerFactory.getLogger(AnnotatedResourcePlugin.class);


    private static Set<Class> supporedAnnotations = new HashSet<>(Arrays.asList(
            ResourcePluginFactory.class,
            ResourcePluginChainableInjectionProvider.class
    ));

    public static boolean containsAnnotations(Plugin plugin) {
        return AnnotatedRunnerBase.containsRunnerAnnotations(plugin, supporedAnnotations);
    }

    public static <T extends Plugin> void inject(ResourceFactory resourceFactory, T plugin) {
        for (Method method : ReflectionUtils.findMethods(plugin.getClass())) {
            if (method.getAnnotation(ResourcePluginFactory.class) != null) {
                try {
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    if (method.getParameterCount() == 1 && method.getParameterTypes()[0].isAssignableFrom(ResourceFactory.class)) {
                        method.invoke(plugin, resourceFactory);
                    } else {
                        throw new RuntimeException("method with ResourcePluginFactory must have a parameter of type ResourceFactory");
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static <T extends Plugin> ChainableInjectionPointProvider getChainableInjectionPointProvider(T plugin, InjectionFinder injectionFinder) {
        for (Method method : ReflectionUtils.findMethods(plugin.getClass())) {
            if (method.getAnnotation(ResourcePluginChainableInjectionProvider.class) != null) {
                try {
                    if (!ChainableInjectionPointProvider.class.isAssignableFrom(method.getReturnType())) {
                        throw new RuntimeException("method with ResourcePluginChainableInjectionProvider must return a ChainableInjectionPointProvider class");
                    } else if (method.getParameterCount() != 1 || !InjectionFinder.class.isAssignableFrom(method.getParameterTypes()[0])) {
                        throw new RuntimeException("method with ResourcePluginChainableInjectionProvider must have one parameter of type InjectionFinder");
                    } else {
                        if (!method.isAccessible()) {
                            method.setAccessible(true);
                        }
                        return (ChainableInjectionPointProvider) method.invoke(plugin, injectionFinder);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException("Could not find method annotated with ResourcePluginChainableInjectionProvider");
    }

    public static <T extends Plugin> boolean hasChainableAnnotaion(T plugin) {
        return AnnotatedRunnerBase.containsRunnerAnnotations(plugin,
                new HashSet<>(Arrays.asList(ResourcePluginChainableInjectionProvider.class)));
    }
}

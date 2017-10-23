package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.internal.annotation.ReflectionUtils;
import org.hrodberaht.injection.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.plugin.junit.resources.ChainableInjectionPointProvider;
import org.hrodberaht.injection.plugin.junit.spi.Plugin;
import org.hrodberaht.injection.plugin.junit.spi.annotation.ResourcePluginChainableInjectionProvider;
import org.hrodberaht.injection.plugin.junit.spi.annotation.ResourcePluginFactory;
import org.hrodberaht.injection.spi.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotatedResourcePlugin {
    private static final Logger LOG = LoggerFactory.getLogger(AnnotatedResourcePlugin.class);

    private static Map<Class, List<Method>> classResourcePluginFactoryMethodMap = new ConcurrentHashMap<>();
    private static Map<Class, Method> classChainableInjectionProviderMethodMap = new ConcurrentHashMap<>();


    private static Set<Class> supporedAnnotations = new HashSet<>(Arrays.asList(
            ResourcePluginFactory.class,
            ResourcePluginChainableInjectionProvider.class
    ));

    public static boolean containsAnnotations(Plugin plugin) {
        return AnnotatedRunnerBase.containsRunnerAnnotations(plugin, supporedAnnotations);
    }

    public static <T extends Plugin> void inject(ResourceFactory resourceFactory, T plugin) {
        List<Method> methodList = classResourcePluginFactoryMethodMap.computeIfAbsent(plugin.getClass(), aClass -> {
            List<Method> methodListInner = new ArrayList<>();
            for (Method method : ReflectionUtils.findMethods(aClass)) {
                if (method.getAnnotation(ResourcePluginFactory.class) != null) {
                    LOG.info("found ResourcePluginFactory at {} in {}", method.getName(), aClass.getName());
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    methodListInner.add(method);
                }
            }
            return methodListInner;
        });

        methodList.forEach(method -> {
            try {
                if (method.getParameterCount() == 1 && method.getParameterTypes()[0].isAssignableFrom(ResourceFactory.class)) {
                    method.invoke(plugin, resourceFactory);
                } else {
                    throw new InjectRuntimeException("method with ResourcePluginFactory must have a parameter of type ResourceFactory");
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new InjectRuntimeException(e);
            }
        });



    }

    public static <T extends Plugin> ChainableInjectionPointProvider getChainableInjectionPointProvider(T plugin, InjectionFinder injectionFinder) {
        Method methodToReturn = classChainableInjectionProviderMethodMap.computeIfAbsent(plugin.getClass(), aClass -> {
            for (Method method : ReflectionUtils.findMethods(plugin.getClass())) {
                if (method.getAnnotation(ResourcePluginChainableInjectionProvider.class) != null) {
                    return evaluateInjectionProviderMethod(aClass, method);
                }
            }
            return null;
        });
        try {
            if (methodToReturn != null) {
                return (ChainableInjectionPointProvider) methodToReturn.invoke(plugin, injectionFinder);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InjectRuntimeException(e);
        }
        throw new InjectRuntimeException("Could not find method annotated with ResourcePluginChainableInjectionProvider");
    }

    private static Method evaluateInjectionProviderMethod(Class aClass, Method method) {
        if (!ChainableInjectionPointProvider.class.isAssignableFrom(method.getReturnType())) {
            throw new InjectRuntimeException("method with ResourcePluginChainableInjectionProvider must return a ChainableInjectionPointProvider class");
        } else if (method.getParameterCount() != 1 || !InjectionFinder.class.isAssignableFrom(method.getParameterTypes()[0])) {
            throw new InjectRuntimeException("method with ResourcePluginChainableInjectionProvider must have one parameter of type InjectionFinder");
        } else {
            LOG.info("found ResourcePluginChainableInjectionProvider at {} in {}", method.getName(), aClass.getName());
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            return method;
        }
    }

    public static <T extends Plugin> boolean hasChainableAnnotaion(T plugin) {
        return AnnotatedRunnerBase.containsRunnerAnnotations(plugin,
                new HashSet<>(Arrays.asList(ResourcePluginChainableInjectionProvider.class)));
    }
}

package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.internal.annotation.ReflectionUtils;
import org.hrodberaht.injection.plugin.junit.spi.Plugin;
import org.hrodberaht.injection.plugin.junit.spi.annotation.RunnerPluginAfterClassTest;
import org.hrodberaht.injection.plugin.junit.spi.annotation.RunnerPluginAfterContainerCreation;
import org.hrodberaht.injection.plugin.junit.spi.annotation.RunnerPluginAfterTest;
import org.hrodberaht.injection.plugin.junit.spi.annotation.RunnerPluginBeforeClassTest;
import org.hrodberaht.injection.plugin.junit.spi.annotation.RunnerPluginBeforeContainerCreation;
import org.hrodberaht.injection.plugin.junit.spi.annotation.RunnerPluginBeforeTest;
import org.hrodberaht.injection.register.InjectionRegister;
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
    private final Map<Class, Plugin> annotatedPlugin = new ConcurrentHashMap<>();

    private static Set<Class> supporedAnnotations = new HashSet<>(Arrays.asList(
            RunnerPluginBeforeContainerCreation.class,
            RunnerPluginAfterContainerCreation.class,
            RunnerPluginBeforeClassTest.class,
            RunnerPluginAfterClassTest.class,
            RunnerPluginBeforeTest.class,
            RunnerPluginAfterTest.class
            ));

    public static boolean containsRunnerAnnotations(Plugin plugin){

        for(Method method : ReflectionUtils.findMethods(plugin.getClass())){
            for(Class annotationCLass: supporedAnnotations){
                if(method.getAnnotation(annotationCLass) != null){
                    return true;
                }
            }
        }
        return false;
    }

    public Plugin addPlugin(Plugin plugin){
        if (annotatedPlugin.get(plugin.getClass()) != null) {
            LOG.info("reused plugin "+plugin.getClass());
            return annotatedPlugin.get(plugin.getClass());
        }
        LOG.info("added plugin "+plugin.getClass());
        annotatedPlugin.put(plugin.getClass(), plugin);
        return plugin;
    }


    public void findAnnotationAndInvokeMethod(PluginRunnerBase runnerBase, InjectionRegister injectionRegister, Class<Annotation> annotation) {
        annotatedPlugin.forEach((aClass, plugin) -> {
            runnerBase.runIfActive(aClass, () -> {
                AnnotationKey annotationKey = new AnnotationKey(aClass, annotation);
                List<Method> foundMethods = annotationKeyMethodsMap.computeIfAbsent(annotationKey, annotationKey1 -> {
                    List<Method> foundMethodsInner = new ArrayList<>();
                    for(Method method : ReflectionUtils.findMethods(aClass)){
                        if(method.getAnnotation(annotation) != null){
                            if(!method.isAccessible()){
                                method.setAccessible(true);
                            }
                            foundMethodsInner.add(method);
                        }
                    }
                    return foundMethodsInner;
                });


                foundMethods.forEach(method -> {
                    try {
                        if(injectionRegister != null && method.getParameterCount() == 1 && method.getParameterTypes()[0].isAssignableFrom(InjectionRegister.class)){
                            method.invoke(plugin, injectionRegister);
                        }else{
                            method.invoke(plugin);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
        });

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

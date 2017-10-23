package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.internal.annotation.ReflectionUtils;
import org.hrodberaht.injection.plugin.junit.spi.Plugin;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class AnnotatedRunnerBase {

    private static Map<Class, List<Method>> methodListMap = new ConcurrentHashMap<>();

    static boolean containsRunnerAnnotations(Plugin plugin, Set<Class> supportedAnnotations) {
        List<Method> methodList = methodListMap.computeIfAbsent(plugin.getClass(), ReflectionUtils::findMethods);
        for (Method method : methodList) {
            for (Class annotationCLass : supportedAnnotations) {
                if (method.getAnnotation(annotationCLass) != null) {
                    return true;
                }
            }
        }
        return false;
    }

}

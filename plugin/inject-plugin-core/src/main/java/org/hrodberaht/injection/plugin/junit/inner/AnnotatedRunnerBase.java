package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.internal.annotation.ReflectionUtils;
import org.hrodberaht.injection.plugin.junit.spi.Plugin;

import java.lang.reflect.Method;
import java.util.Set;

class AnnotatedRunnerBase {

    static boolean containsRunnerAnnotations(Plugin plugin, Set<Class> supportedAnnotations) {

        for (Method method : ReflectionUtils.findMethods(plugin.getClass())) {
            for (Class annotationCLass : supportedAnnotations) {
                if (method.getAnnotation(annotationCLass) != null) {
                    return true;
                }
            }
        }
        return false;
    }

}

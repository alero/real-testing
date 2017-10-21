package org.hrodberaht.injection.plugin.junit.resources;

import org.hrodberaht.injection.internal.annotation.AnnotationInjection;
import org.hrodberaht.injection.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.internal.annotation.InjectionPoint;
import org.hrodberaht.injection.spi.ContainerConfig;

import java.lang.reflect.Method;
import java.util.List;

public class ChainableInjectionPointProvider implements InjectionFinder {
    private final InjectionFinder injectionFinder;

    public ChainableInjectionPointProvider(InjectionFinder injectionFinder) {
        this.injectionFinder = injectionFinder;
    }

    @Override
    public List<InjectionPoint> findInjectionPoints(Class instanceClass, AnnotationInjection annotationInjection) {
        return injectionFinder.findInjectionPoints(instanceClass, annotationInjection);
    }

    @Override
    public Method findPostConstruct(Class instanceClass) {
        return injectionFinder.findPostConstruct(instanceClass);
    }

    @Override
    public Object extendedInjection(Object instance) {
        return injectionFinder.extendedInjection(instance);
    }

    @Override
    public ContainerConfig getContainerConfig() {
        return injectionFinder.getContainerConfig();
    }
}

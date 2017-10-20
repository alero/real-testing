package org.hrodberaht.injection.plugin.junit.resources;

import org.hrodberaht.injection.internal.annotation.AnnotationInjection;
import org.hrodberaht.injection.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.internal.annotation.InjectionPoint;
import org.hrodberaht.injection.spi.ContainerConfig;

import java.lang.reflect.Method;
import java.util.List;

public class ChainableInjectionPointProvider implements InjectionFinder{
    private final InjectionFinder injectionFinder;

    public ChainableInjectionPointProvider(InjectionFinder injectionFinder) {
        this.injectionFinder = injectionFinder;
    }


    @Override
    public List<InjectionPoint> findInjectionPoints(Class service, AnnotationInjection annotationInjection) {
        return injectionFinder.findInjectionPoints(service, annotationInjection);
    }

    @Override
    public Method findPostConstruct(Class serviceClass) {
        return injectionFinder.findPostConstruct(serviceClass);
    }

    @Override
    public void extendedInjection(Object service) {
        injectionFinder.extendedInjection(service);
    }

    @Override
    public ContainerConfig getContainerConfig() {
        return injectionFinder.getContainerConfig();
    }
}

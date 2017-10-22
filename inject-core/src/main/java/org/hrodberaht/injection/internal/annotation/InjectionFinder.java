package org.hrodberaht.injection.internal.annotation;

import org.hrodberaht.injection.spi.ContainerConfig;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Injection Extension JUnit
 *
 * @author Robert Alexandersson
 * 2010-sep-23 20:33:47
 * @version 1.0
 * @since 1.0
 */
public interface InjectionFinder {

    enum InjectionType {EXTENDED, OVERRIDDEN}

    List<InjectionPoint> findInjectionPoints(Class instanceClass, AnnotationInjection annotationInjection);

    Method findPostConstruct(Class instanceClass);

    Object extendedInjection(Object instanc);

    ContainerConfig getContainerConfig();

    default InjectionType getInjectionType() {
        return InjectionType.EXTENDED;
    }


}

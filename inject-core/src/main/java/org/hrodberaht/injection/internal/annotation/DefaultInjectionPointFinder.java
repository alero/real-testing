package org.hrodberaht.injection.internal.annotation;

import org.hrodberaht.injection.annotation.PostConstruct;
import org.hrodberaht.injection.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.spi.ContainerConfig;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Injection Extension JUnit
 *
 * @author Robert Alexandersson
 *         2010-sep-23 20:34:49
 * @version 1.0
 * @since 1.0
 */
public class DefaultInjectionPointFinder implements InjectionFinder {

    private ContainerConfig containerConfig;

    public DefaultInjectionPointFinder() {
    }

    public DefaultInjectionPointFinder(ContainerConfig containerConfig) {
        this.containerConfig = containerConfig;
    }

    /**
     * Finds all injection points for a class analysing fields and methods
     *
     * @param service the class to analyze
     * @return found injection points
     */
    public List<InjectionPoint> findInjectionPoints(Class service, AnnotationInjection annotationInjection) {
        List<InjectionPoint> injectionPoints = new ArrayList<InjectionPoint>();
        if (service == null) {
            return injectionPoints;
        }
        List<Method> allMethods = ReflectionUtils.findMethods(service);
        List<Member> allMembers = ReflectionUtils.findMembers(service);
        for (Member member : allMembers) {
            if (member instanceof Field) {
                Field field = (Field) member;
                if (fieldNeedsInjection(field)) {
                    injectionPoints.add(new InjectionPoint(field, annotationInjection));
                }
            } else if (member instanceof Method) {
                Method method = (Method) member;
                if (methodNeedsInjection(method) &&
                        // This makes sure that overridden methods are not injected
                        !ReflectionUtils.isOverridden(method, allMethods)) {
                    injectionPoints.add(new InjectionPoint(method, annotationInjection));
                }
            } else {
                throw new UnsupportedOperationException("Unsupported member: " + member);
            }
        }
        return injectionPoints;
    }

    public Method findPostConstruct(Class serviceClass) {
        List<Method> allMethods = ReflectionUtils.findMethods(serviceClass);
        Method foundMethod = null;
        for (Method method : allMethods) {
            if (methodHasPostConstruct(method)) {
                if (foundMethod != null) {
                    throw new InjectRuntimeException("Several PostConstruct annotations found, make sure there is only one");
                }
                foundMethod = method;
            }
        }
        return foundMethod;
    }

    public void extendedInjection(Object service) {
        // Default does not perform extended injection
    }

    public ContainerConfig getContainerConfig() {
        return containerConfig;
    }

    /**
     * Intended for override to support other annotations
     *
     * @param method
     * @return
     */
    protected boolean hasPostConstructAnnotation(Method method) {
        return method.isAnnotationPresent(PostConstruct.class);
    }

    /**
     * Intended for override to support other annotations
     *
     * @param method
     * @return
     */
    protected boolean hasInjectAnnotationOnMethod(Method method) {
        return method.isAnnotationPresent(InjectionUtils.INJECT);
    }

    /**
     * Intended for override to support other annotations
     *
     * @param field
     * @return
     */
    protected boolean hasInjectAnnotationOnField(Field field) {
        return field.isAnnotationPresent(InjectionUtils.INJECT);
    }

    private boolean methodHasPostConstruct(Method method) {
        return !ReflectionUtils.isStatic(method)
                && hasPostConstructAnnotation(method);
    }

    private boolean methodNeedsInjection(Method method) {
        return !ReflectionUtils.isStatic(method)
                && hasInjectAnnotationOnMethod(method);
    }

    private boolean fieldNeedsInjection(Field field) {
        return !ReflectionUtils.isStatic(field)
                && !ReflectionUtils.isFinal(field)
                && hasInjectAnnotationOnField(field);
    }


}

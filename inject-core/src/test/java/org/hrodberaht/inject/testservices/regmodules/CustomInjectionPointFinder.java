package org.hrodberaht.inject.testservices.regmodules;

import org.hrodberaht.inject.annotation.ExtendedResourceInjection;
import org.hrodberaht.inject.testservices.annotated.Injected;
import org.hrodberaht.inject.testservices.annotated.PostConstructInit;
import org.hrodberaht.injection.internal.annotation.DefaultInjectionPointFinder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2013-10-08
 * Time: 10:58
 * To change this template use File | Settings | File Templates.
 */
public class CustomInjectionPointFinder extends DefaultInjectionPointFinder {
    @Override
    protected boolean hasInjectAnnotationOnMethod(Method method) {
        return method.isAnnotationPresent(Injected.class) ||
                super.hasInjectAnnotationOnMethod(method);
    }

    @Override
    protected boolean hasInjectAnnotationOnField(Field field) {
        return field.isAnnotationPresent(Injected.class) ||
                super.hasInjectAnnotationOnField(field);
    }

    @Override
    protected boolean hasPostConstructAnnotation(Method method) {
        return method.isAnnotationPresent(PostConstructInit.class) ||
                super.hasPostConstructAnnotation(method);
    }

    @Override
    public void extendedInjection(Object service) {
        ExtendedResourceInjection.injectText(service, "Text");
    }
}

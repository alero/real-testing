package org.hrodberaht.injection.extensions.cdi.cdiext;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-12-03
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
public class MethodClassHolder {

    private Class aClass;
    private Method method;

    public MethodClassHolder(Class aClass, Method method) {
        this.aClass = aClass;
        this.method = method;
    }

    public Class getaClass() {
        return aClass;
    }

    public Method getMethod() {
        return method;
    }
}

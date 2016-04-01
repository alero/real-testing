package org.hrodberaht.injection.extensions.spring.instance;

import org.hrodberaht.injection.internal.annotation.InjectionPoint;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by alexbrob on 2016-04-01.
 */
public class SpringInjectionPoint extends InjectionPoint {

    private String name;

    public SpringInjectionPoint(Field field, String name) {
        super(field);
        this.name = name;
    }

    public SpringInjectionPoint(Method method, String name) {
        super(method);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

package org.hrodberaht.injection.plugin.junit.spring.injector;

import org.hrodberaht.injection.internal.annotation.InjectionPoint;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by alexbrob on 2016-04-01.
 */
public class SpringInjectionPoint extends InjectionPoint {

    private String name;
    private Class interfaceClass;

    SpringInjectionPoint(Field field, Class interfaceClass) {
        super(field);
        this.interfaceClass = interfaceClass;
    }

    SpringInjectionPoint(Field field, String name) {
        super(field);
        this.name = name;
    }

    SpringInjectionPoint(Method method, String name) {
        super(method);
        this.name = name;
    }

    String getName() {
        return name;
    }

    Class getInterfaceClass() {
        return interfaceClass;
    }

    public String getDisplayName(){
        if(interfaceClass != null){
            return "type:"+interfaceClass.getName();
        }
        return "name:"+name;
    }
}

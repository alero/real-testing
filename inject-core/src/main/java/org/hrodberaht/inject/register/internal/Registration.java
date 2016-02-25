package org.hrodberaht.inject.register.internal;

import org.hrodberaht.inject.register.InjectionFactory;
import org.hrodberaht.inject.register.VariableInjectionFactory;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jun-17 20:22:48
 * @version 1.0
 * @since 1.0
 */
public interface Registration {
    Registration annotated(Class<? extends Annotation> annotation);

    Registration named(String named);

    Registration with(Class theService);

    Registration withInstance(Object aSingleton);

    Registration withFactory(InjectionFactory aFactory);

    Registration withVariableFactory(VariableInjectionFactory variableInjectionFactory);
}

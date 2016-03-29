package com.hrodberaht.inject.extensions.transaction.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-12 19:47:33
 * @version 1.0
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface InjectionContainerContext {
    Class<? extends InjectionContainerCreator> value();
    boolean disableRequiresNewTransaction() default false;
}

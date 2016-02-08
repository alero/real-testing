package org.hrodberaht.inject.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-sep-23 20:52:55
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface PostConstruct {
}

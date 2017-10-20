package org.hrodberaht.injection.register;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-jun-06 02:36:14
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RUNTIME)
public @interface Overrides {

    Class className();
}

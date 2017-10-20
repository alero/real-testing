package org.hrodberaht.inject.testservices.annotated;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * 2010-jun-06 04:24:35
 * @version 1.0
 * @since 1.0
 */
@Retention(RUNTIME)
@Qualifier
public @interface Spare {
}

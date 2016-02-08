package org.hrodberaht.inject.annotation;

/**
 * Projectname
 *
 * @author Robert Alexandersson
 *         2010-sep-26 22:39:09
 * @version 1.0
 * @since 1.0
 */
public interface VariableProvider<T, K> {
    T get(K variable);
}

package org.hrodberaht.inject.internal.annotation.scope;

import org.hrodberaht.inject.SimpleInjection;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-jun-06 02:46:05
 * @version 1.0
 * @since 1.0
 */
public interface ScopeHandler {
    Object getInstance();

    void addScope(Object instance);

    SimpleInjection.Scope getScope();

    boolean isInstanceCreated();
}

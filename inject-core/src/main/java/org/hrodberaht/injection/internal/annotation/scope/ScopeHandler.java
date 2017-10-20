package org.hrodberaht.injection.internal.annotation.scope;

import org.hrodberaht.injection.internal.InjectionContainerManager;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-jun-06 02:46:05
 * @version 1.0
 * @since 1.0
 */
public interface ScopeHandler {
    Object getInstance();

    void addInstance(Object instance);

    InjectionContainerManager.Scope getScope();

    boolean isInstanceCreated();
}

package org.hrodberaht.injection.internal.annotation.scope;

import org.hrodberaht.injection.internal.InjectionContainerManager;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-jun-06 02:45:49
 * @version 1.0
 * @since 1.0
 */
public class SingletonScopeHandler implements ScopeHandler {

    private Object singleton;

    public Object getInstance() {
        return singleton;
    }

    public void addInstance(Object instance) {
        singleton = instance;
    }

    public InjectionContainerManager.Scope getScope() {
        return InjectionContainerManager.Scope.SINGLETON;
    }

    public boolean isInstanceCreated() {
        return false;
    }
}

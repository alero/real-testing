package org.hrodberaht.inject.internal.annotation.scope;

import org.hrodberaht.inject.SimpleInjection;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-jun-06 02:45:49
 * @version 1.0
 * @since 1.0
 */
public class SingletonScopeHandler implements ScopeHandler {

    private Object singleton;

    public Object getInstance() {
        return singleton;
    }

    public void addScope(Object instance) {
        singleton = instance;
    }

    public SimpleInjection.Scope getScope() {
        return SimpleInjection.Scope.SINGLETON;
    }

    public boolean isInstanceCreated() {
        return false;
    }
}

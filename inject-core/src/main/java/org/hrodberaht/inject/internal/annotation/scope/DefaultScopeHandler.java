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
public class DefaultScopeHandler implements ScopeHandler {


    public Object getInstance() {
        return null;
    }

    public void addScope(Object instance) {

    }

    public SimpleInjection.Scope getScope() {
        return SimpleInjection.Scope.NEW;
    }

    public boolean isInstanceCreated() {
        return true;
    }


}
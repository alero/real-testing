package org.hrodberaht.injection.internal.annotation.scope;

import org.hrodberaht.injection.InjectionContainerManager;

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

    public void addInstance(Object instance) {

    }

    public InjectionContainerManager.Scope getScope() {
        return InjectionContainerManager.Scope.NEW;
    }

    public boolean isInstanceCreated() {
        return true;
    }


}
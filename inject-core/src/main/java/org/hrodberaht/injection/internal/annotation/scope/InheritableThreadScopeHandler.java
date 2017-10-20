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
public class InheritableThreadScopeHandler implements ScopeHandler {

    private InheritableThreadLocal<Object> placeHolder = new InheritableThreadLocal<Object>();


    public Object getInstance() {
        return placeHolder.get();
    }

    public void addInstance(Object instance) {
        placeHolder.set(instance);
    }

    public InjectionContainerManager.Scope getScope() {
        return InjectionContainerManager.Scope.INHERITABLE_THREAD;
    }

    public boolean isInstanceCreated() {
        return false;
    }
}
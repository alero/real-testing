package org.hrodberaht.inject.internal.annotation.scope;

import org.hrodberaht.inject.ScopeContainer;
import org.hrodberaht.inject.register.InjectionFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Robert Work
 * Date: 2010-aug-18
 * Time: 15:51:41
 * To change this template use File | Settings | File Templates.
 */
public class FactoryScopeHandler implements ScopeHandler {

    InjectionFactory injectionFactory;

    public FactoryScopeHandler(InjectionFactory theFactory) {
        this.injectionFactory = theFactory;
    }

    public Object getInstance() {
        return injectionFactory.getInstance();
    }

    public void addScope(Object instance) {
        throw new IllegalAccessError("Factory can not have instance set");
    }

    public ScopeContainer.Scope getScope() {
        return ScopeContainer.Scope.NEW;
    }

    public boolean isInstanceCreated() {
        return injectionFactory.newObjectOnInstance();
    }
}

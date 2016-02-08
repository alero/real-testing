package org.hrodberaht.inject.internal.annotation.scope;

import org.hrodberaht.inject.ScopeContainer;
import org.hrodberaht.inject.register.VariableInjectionFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Robert Work
 * Date: 2010-aug-18
 * Time: 15:51:41
 * To change this template use File | Settings | File Templates.
 */
public class VariableFactoryScopeHandler implements VariableScopeHandler {

    VariableInjectionFactory injectionFactory;

    public VariableFactoryScopeHandler(VariableInjectionFactory theFactory) {
        this.injectionFactory = theFactory;
    }

    @SuppressWarnings(value = "unchecked")
    public Class getInstanceClass(Object variable) {
        return injectionFactory.getInstanceClass(variable);
    }

    public void addScope(Object instance) {
        throw new IllegalAccessError("Factory can not have instance set");
    }

    public Object getInstance() {
        return null;
    }

    public ScopeContainer.Scope getScope() {
        return null;
    }

    public boolean isInstanceCreated() {
        return true;
    }
}

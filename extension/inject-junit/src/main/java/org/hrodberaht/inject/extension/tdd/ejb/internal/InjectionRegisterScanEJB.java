package org.hrodberaht.inject.extension.tdd.ejb.internal;

import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.ScopeContainer;
import org.hrodberaht.inject.config.InjectionRegisterScanBase;
import org.hrodberaht.inject.register.InjectionRegister;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.Stateless;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:45:53
 * @version 1.0
 * @since 1.0
 */
public class InjectionRegisterScanEJB extends InjectionRegisterScanBase {


    public InjectionRegisterScanEJB(InjectionRegister registerModule) {
        super(registerModule);
    }

    @Override
    public InjectContainer getInjectContainer() {
        return container;
    }

    @Override
    public InjectionRegisterScanEJB clone() {
        InjectionRegisterScanEJB clone = new InjectionRegisterScanEJB(referedRegister);
        try {
            clone.container = this.container.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }

    public boolean isInterfaceAnnotated(Class aClazz) {
        if (aClazz.isAnnotationPresent(Local.class)) {
            return true;
        } else if (aClazz.isAnnotationPresent(Remote.class)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isServiceAnnotated(Class aClazz) {
        if (aClazz.isAnnotationPresent(Stateless.class)) {
            return true;
        } else if (aClazz.isAnnotationPresent(Stateful.class)) {
            return true;
        }
        return false;
    }



    public ScopeContainer.Scope getScope(Class serviceClass) {
        if (serviceClass.isAnnotationPresent(Stateless.class)) {
            return ScopeContainer.Scope.SINGLETON;
        }
        return ScopeContainer.Scope.NEW;
    }

}

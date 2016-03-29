package org.hrodberaht.injection.extensions.spring;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.ScopeContainer;
import org.hrodberaht.injection.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.register.InjectionRegister;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:45:53
 * @version 1.0
 * @since 1.0
 */
public class InjectionRegisterScanSpring extends InjectionRegisterScanBase {

    private boolean hasStatelessAnnotationInClassPath = true;
    private boolean hasSingletonAnnotationInClassPath = true;

    public InjectionRegisterScanSpring(InjectionRegister injectionRegister) {
        super(injectionRegister);
    }

    public InjectionRegisterScanSpring() {
        super();
    }

    @Override
    public InjectContainer getInjectContainer() {
        return container;
    }

    @Override
    public InjectionRegisterScanSpring clone() {
        InjectionRegisterScanSpring clone = new InjectionRegisterScanSpring(super.referedRegister);
        try {
            clone.container = this.container.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }

    public boolean isInterfaceAnnotated(Class aClazz) {
        return true;
    }

    public boolean isServiceAnnotated(Class aClazz) {
        if (aClazz.isAnnotationPresent(Component.class)) {
            return true;
        } else if (aClazz.isAnnotationPresent(Service.class)) {
            return true;
        } else if (aClazz.isAnnotationPresent(Controller.class)) {
            return true;
        }
        return false;
    }




    public ScopeContainer.Scope getScope(Class serviceClass) {

        if (hasStatelessAnnotationInClassPath) {
            try {
                // if (serviceClass.isAnnotationPresent(javax.ejb.Stateless.class)) {
                //    return ScopeContainer.Scope.SINGLETON;
                //}
            } catch (NoClassDefFoundError e) {
                hasStatelessAnnotationInClassPath = false;
            }
        }
        if (hasSingletonAnnotationInClassPath) {
            try {
                if (serviceClass.isAnnotationPresent(javax.inject.Singleton.class)) {
                    return ScopeContainer.Scope.SINGLETON;
                }
            } catch (NoClassDefFoundError e) {
                hasSingletonAnnotationInClassPath = false;
            }
        }
        return ScopeContainer.Scope.SINGLETON;
    }

}
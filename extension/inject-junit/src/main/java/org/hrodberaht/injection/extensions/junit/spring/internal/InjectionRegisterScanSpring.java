package org.hrodberaht.injection.extensions.junit.spring.internal;

import org.hrodberaht.injection.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.internal.ScopeContainer;
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


    public InjectionRegisterScanSpring(InjectionRegister injectionRegister) {
        super(injectionRegister);
    }

    public InjectionRegisterScanSpring() {
    }



    @Override
    public InjectionRegisterScanSpring clone() {
        InjectionRegisterScanSpring clone = new InjectionRegisterScanSpring();
        try {
            clone.container = this.container.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }

    public boolean isInterfaceAnnotated(Class aClazz) {
        return false;
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
        // Spring uses singleton as default
        return ScopeContainer.Scope.SINGLETON;
    }


}

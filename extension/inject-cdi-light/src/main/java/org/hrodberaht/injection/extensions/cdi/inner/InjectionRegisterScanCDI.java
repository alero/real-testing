package org.hrodberaht.injection.extensions.cdi.inner;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.internal.ScopeContainer;
import org.hrodberaht.injection.register.InjectionRegister;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:45:53
 * @version 1.0
 * @since 1.0
 */
public class InjectionRegisterScanCDI extends InjectionRegisterScanBase {

    private boolean hasStatelessAnnotationInClassPath = true;
    private boolean hasSingletonAnnotationInClassPath = true;

    public InjectionRegisterScanCDI(InjectionRegister injectionRegister) {
        super(injectionRegister);
    }

    public InjectionRegisterScanCDI() {
        super();
    }

    @Override
    public InjectContainer getInjectContainer() {
        return container;
    }

    @Override
    public InjectionRegisterScanCDI clone() {
        InjectionRegisterScanCDI clone = new InjectionRegisterScanCDI(super.referedRegister);
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

    @Override
    public boolean isServiceAnnotated(Class aClazz) {
        return false;
    }



    public ScopeContainer.Scope getScope(Class serviceClass) {

        if (hasStatelessAnnotationInClassPath) {
            try {
                if (serviceClass.isAnnotationPresent(javax.ejb.Stateless.class)) {
                    return ScopeContainer.Scope.SINGLETON;
                }
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
        return ScopeContainer.Scope.NEW;
    }

}

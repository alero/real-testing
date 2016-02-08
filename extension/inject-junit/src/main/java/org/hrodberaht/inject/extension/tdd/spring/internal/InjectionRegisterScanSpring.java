package org.hrodberaht.inject.extension.tdd.spring.internal;

import org.hrodberaht.inject.ScopeContainer;
import org.hrodberaht.inject.extension.tdd.internal.InjectionRegisterScanBase;
import org.hrodberaht.inject.internal.exception.InjectRuntimeException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Unit Test EJB (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:45:53
 * @version 1.0
 * @since 1.0
 */
public class InjectionRegisterScanSpring extends InjectionRegisterScanBase {


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

    protected boolean isInterfaceAnnotated(Class aClazz) {
        return false;
    }

    protected boolean isServiceAnnotated(Class aClazz) {
        if (aClazz.isAnnotationPresent(Component.class)) {
            return true;
        } else if (aClazz.isAnnotationPresent(Service.class)) {
            return true;
        } else if (aClazz.isAnnotationPresent(Controller.class)) {
            return true;
        }
        return false;
    }

    protected Class findServiceImplementation(Class aClazz, List<Class> listOfClasses) {
        Class foundServiceImplementation = null;
        for (Class aServiceClass : listOfClasses) {

            if (!aServiceClass.isInterface()
                    && !aServiceClass.isAnnotation()
                    && !Modifier.isAbstract(aServiceClass.getModifiers())
                    ) {
                for (Class aInterface : aServiceClass.getInterfaces()) {
                    if (aInterface == aClazz) {
                        if (foundServiceImplementation != null) {
                            throw new InjectRuntimeException("ServiceInterface implemented in two classes {0} and {1}"
                                    , foundServiceImplementation, aServiceClass
                            );
                        }
                        foundServiceImplementation = aServiceClass;
                    }
                }
            }
        }

        return foundServiceImplementation;
    }

    protected ScopeContainer.Scope getScope(Class serviceClass) {
        // Spring uses singleton as default
        return ScopeContainer.Scope.SINGLETON;
    }

}

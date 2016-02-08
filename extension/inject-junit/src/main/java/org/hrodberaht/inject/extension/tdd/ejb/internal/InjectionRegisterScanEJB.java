package org.hrodberaht.inject.extension.tdd.ejb.internal;

import org.hrodberaht.inject.ScopeContainer;
import org.hrodberaht.inject.extension.tdd.internal.InjectionRegisterScanBase;
import org.hrodberaht.inject.internal.exception.InjectRuntimeException;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
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
public class InjectionRegisterScanEJB extends InjectionRegisterScanBase {


    @Override
    public InjectionRegisterScanEJB clone() {
        InjectionRegisterScanEJB clone = new InjectionRegisterScanEJB();
        try {
            clone.container = this.container.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }

    protected boolean isInterfaceAnnotated(Class aClazz) {
        if (aClazz.isAnnotationPresent(Local.class)) {
            return true;
        } else if (aClazz.isAnnotationPresent(Remote.class)) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean isServiceAnnotated(Class aClazz) {
        if (aClazz.isAnnotationPresent(Stateless.class)) {
            return true;
        } else if (aClazz.isAnnotationPresent(Stateful.class)) {
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
        if (serviceClass.isAnnotationPresent(Stateless.class)) {
            return ScopeContainer.Scope.SINGLETON;
        }
        return ScopeContainer.Scope.NEW;
    }

}

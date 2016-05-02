package org.hrodberaht.injection.extensions.junit.spring;

import org.hrodberaht.injection.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.config.InjectionStoreFactory;
import org.hrodberaht.injection.extensions.junit.internal.JunitContainerConfigBase;
import org.hrodberaht.injection.extensions.junit.spring.internal.InjectionRegisterScanSpring;
import org.hrodberaht.injection.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.spi.module.CustomInjectionPointFinderModule;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Injection Extension JUnit
 *
 * @author Robert Alexandersson
 *         2010-okt-26 18:59:45
 * @version 1.0
 * @since 1.0
 */
public abstract class SpringContainerConfigBase extends JunitContainerConfigBase<InjectionRegisterScanSpring> {

    protected SpringContainerConfigBase() {

    }

    protected InjectionRegister preScanModuleRegistration() {
        InjectionRegister injectionRegisterModule = InjectionStoreFactory.getInjectionRegister();
        injectionRegisterModule.register(new CustomInjectionPointFinderModule(new DefaultInjectionPointFinder(this) {
            @Override
            protected boolean hasInjectAnnotationOnMethod(Method method) {
                return method.isAnnotationPresent(Autowired.class) ||
                        super.hasInjectAnnotationOnMethod(method);
            }

            @Override
            protected boolean hasInjectAnnotationOnField(Field field) {
                return field.isAnnotationPresent(Autowired.class) ||
                        super.hasInjectAnnotationOnField(field);
            }

            @Override
            protected boolean hasPostConstructAnnotation(Method method) {
                return method.isAnnotationPresent(PostConstruct.class) ||
                        super.hasPostConstructAnnotation(method);
            }

            @Override
            public void extendedInjection(Object service) {
                SpringContainerConfigBase config = (SpringContainerConfigBase) getContainerConfig();
                config.injectResources(service);
            }
        }
        ));
        return injectionRegisterModule;
    }

    protected InjectionRegisterScanBase getScanner(InjectionRegister injectionRegister) {
        return new InjectionRegisterScanSpring(injectionRegister);
    }

    @Override
    protected void injectResources(Object serviceInstance) {
        resourceInjection.injectResources(serviceInstance);
    }
}

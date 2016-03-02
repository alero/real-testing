package org.hrodberaht.inject.extension.tdd.spring;

import org.hrodberaht.inject.InjectionRegisterModule;
import org.hrodberaht.inject.config.InjectionRegisterScanBase;
import org.hrodberaht.inject.extension.tdd.internal.TDDContainerConfigBase;
import org.hrodberaht.inject.extension.tdd.spring.internal.InjectionRegisterScanSpring;
import org.hrodberaht.inject.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.inject.register.InjectionRegister;
import org.hrodberaht.inject.spi.module.CustomInjectionPointFinderModule;
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
public abstract class SpringContainerConfigBase extends TDDContainerConfigBase<InjectionRegisterScanSpring> {

    protected SpringContainerConfigBase() {

    }

    @Override
    protected InjectionRegisterModule preScanModuleRegistration() {
        InjectionRegisterModule injectionRegisterModule = new InjectionRegisterModule();
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

    @Override
    protected InjectionRegisterScanBase getScanner(InjectionRegister injectionRegister) {
        return new InjectionRegisterScanSpring(injectionRegister);
    }


    @Override
    protected void injectResources(Object serviceInstance) {

        injectGenericResources(serviceInstance);
    }
}

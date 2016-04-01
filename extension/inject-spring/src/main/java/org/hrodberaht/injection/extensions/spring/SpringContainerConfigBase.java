package org.hrodberaht.injection.extensions.spring;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.config.jpa.JPAContainerConfigBase;
import org.hrodberaht.injection.extensions.spring.instance.SpringBeanInjector;
import org.hrodberaht.injection.extensions.spring.instance.SpringInject;
import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.internal.InjectionRegisterScan;
import org.hrodberaht.injection.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.spi.ResourceCreator;
import org.hrodberaht.injection.spi.module.CustomInjectionPointFinderModule;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:35:27
 * @version 1.0
 * @since 1.0
 */
public abstract class SpringContainerConfigBase extends JPAContainerConfigBase<InjectionRegisterScanBase> {


    private ApplicationContext context;
    private SpringBeanInjector springBeanInjector;

    protected SpringContainerConfigBase(ResourceCreator resourceCreator) {
        this.resourceCreator = resourceCreator;
    }

    protected SpringContainerConfigBase() {
    }

    public void loadSpringConfig(String springConfig){
        context = new ClassPathXmlApplicationContext("/META-INF/container-spring-config.xml", springConfig){

        };
        springBeanInjector = new SpringBeanInjector(context);
    }

    protected InjectionRegisterModule preScanModuleRegistration() {
        InjectionRegisterModule injectionRegisterModule = new InjectionRegisterModule();
        injectionRegisterModule.register(new CustomInjectionPointFinderModule(new DefaultInjectionPointFinder(this) {
            @Override
            public void extendedInjection(Object service) {
                SpringContainerConfigBase config = (SpringContainerConfigBase) getContainerConfig();
                config.injectResources(service);
                config.injectSpringBeans(service);
            }

            @Override
            protected boolean hasInjectAnnotationOnField(Field field) {
                return field.isAnnotationPresent(SpringInject.class) || super.hasInjectAnnotationOnField(field);
            }

            @Override
            protected boolean hasInjectAnnotationOnMethod(Method method) {
                return method.isAnnotationPresent(SpringInject.class) || super.hasInjectAnnotationOnMethod(method);
            }
        }
        ));
        return injectionRegisterModule;
    }

    private void injectSpringBeans(Object serviceObject) {
        springBeanInjector.inject(serviceObject, getActiveContainer());
    }

    protected InjectionRegisterScanBase getScanner(InjectionRegister injectionRegister) {
        return new InjectionRegisterScan(injectionRegister);
    }

    public abstract InjectContainer createContainer();

    public ResourceCreator getResourceCreator() {
        return resourceCreator;
    }

}

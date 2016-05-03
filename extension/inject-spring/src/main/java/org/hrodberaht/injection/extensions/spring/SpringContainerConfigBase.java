package org.hrodberaht.injection.extensions.spring;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.annotation.PostConstruct;
import org.hrodberaht.injection.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.config.jpa.JPAContainerConfigBase;
import org.hrodberaht.injection.extensions.spring.config.ContainerSpringConfig;
import org.hrodberaht.injection.extensions.spring.instance.SpringBeanInjector;
import org.hrodberaht.injection.extensions.spring.instance.SpringInject;
import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.internal.InjectionRegisterScan;
import org.hrodberaht.injection.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.spi.ResourceCreator;
import org.hrodberaht.injection.spi.module.CustomInjectionPointFinderModule;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:35:27
 * @version 1.0
 * @since 1.0
 */
public abstract class SpringContainerConfigBase extends JPAContainerConfigBase<InjectionRegisterModule> {


    private ApplicationContext context;
    private SpringBeanInjector springBeanInjector;

    protected SpringContainerConfigBase(ResourceCreator resourceCreator) {
        this.resourceCreator = resourceCreator;
    }

    protected SpringContainerConfigBase() {
    }

    protected ApplicationContext getContext() {
        return context;
    }

    public void loadSpringConfig(String... springConfigs) {
        validateEmptyContext(context);
        String testSpringConfig = "/META-INF/container-spring-config.xml";
        String[] config = new String[]{testSpringConfig};
        if (springConfigs != null) {
            Stream<String> stringStream = Stream.concat(Stream.of(springConfigs), Stream.of(testSpringConfig));
            config = stringStream.toArray(String[]::new);
            ;
        }
        context = new ClassPathXmlApplicationContext(config);
        springBeanInjector = new SpringBeanInjector(context);
    }

    public void loadJavaSpringConfig(Class... springConfigs) {
        validateEmptyContext(context);
        Class[] config = new Class[]{ContainerSpringConfig.class};
        if (springConfigs != null) {
            Stream<Class> stringStream = Stream.concat(Stream.of(springConfigs), Stream.of(ContainerSpringConfig.class));
            config = stringStream.toArray(Class[]::new);
        }
        context = new AnnotationConfigApplicationContext(config);
        springBeanInjector = new SpringBeanInjector(context);

    }

    private void validateEmptyContext(ApplicationContext context) {
        if (context != null) {
            throw new IllegalStateException("Context is already loaded, can only be loaded once");
        }
    }

    @Override
    public void addSingletonActiveRegistry() {
        activeRegister.register(new RegistrationModuleAnnotation() {
            @Override
            public void registrations() {
                register(ApplicationContext.class).withFactoryInstance(context);
            }
        });
        super.addSingletonActiveRegistry();
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

            protected boolean hasPostConstructAnnotation(Method method) {
                return method.isAnnotationPresent(javax.annotation.PostConstruct.class) || method.isAnnotationPresent(PostConstruct.class);
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

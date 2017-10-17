package org.hrodberaht.injection.extensions.spring;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.config.jpa.JPAContainerConfigBase;
import org.hrodberaht.injection.extensions.spring.config.ContainerAllSpringConfig;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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

    private static final Logger LOG = LoggerFactory.getLogger(SpringContainerConfigBase.class);

    private static final Map<Class, SpringContainerConfigBase> CACHE = new ConcurrentHashMap<>();
    private ApplicationContext context;
    private SpringBeanInjector springBeanInjector;
    private boolean enableJPA = false;
    private final boolean enabledCache = System.getProperty("hrodberaht.test.spring.cache") != null;

    protected SpringContainerConfigBase(ResourceCreator resourceCreator) {
        this.resourceCreator = resourceCreator;
    }

    protected SpringContainerConfigBase() {
    }

    protected void setEnableJPA(boolean enableJPA) {
        this.enableJPA = enableJPA;
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
        }
        context = new ClassPathXmlApplicationContext(config);
        springBeanInjector = new SpringBeanInjector(context);
    }

    public void loadJavaSpringConfig(Class... springConfigs) {
        SpringContainerConfigBase configBase = CACHE.get(this.getClass());
        if(configBase != null && enabledCache){
            LOG.debug("SpringContainerConfigBase - Using cached SpringApplication for "+this.getClass());
            context = configBase.context;
            springBeanInjector = configBase.springBeanInjector;
        }else {
            LOG.debug("SpringContainerConfigBase - Creating SpringApplication for "+this.getClass());
            validateEmptyContext(context);
            Class[] config = new Class[]{getContainerSpringConfigClass()};
            if (springConfigs != null) {
                Stream<Class> stringStream = Stream.concat(Stream.of(springConfigs), Stream.of(getContainerSpringConfigClass()));
                config = stringStream.toArray(Class[]::new);
            }
            context = new AnnotationConfigApplicationContext(config);
            springBeanInjector = new SpringBeanInjector(context);
            CACHE.put(this.getClass(), this);
        }

    }

    private Class<?> getContainerSpringConfigClass() {
        return enableJPA ? ContainerAllSpringConfig.class : ContainerSpringConfig.class;
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
        }
        ));
        return injectionRegisterModule;
    }

    private void injectSpringBeans(Object serviceObject) {
        springBeanInjector.inject(serviceObject, getActiveContainer());
    }

    @Override
    protected InjectionRegisterScanBase getScanner(InjectionRegister injectionRegister) {
        return new InjectionRegisterScan(injectionRegister);
    }

    @Override
    public abstract InjectContainer createContainer();

    public ResourceCreator getResourceCreator() {
        return resourceCreator;
    }

}

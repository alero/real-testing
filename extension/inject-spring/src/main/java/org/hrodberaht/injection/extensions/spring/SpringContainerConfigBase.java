package org.hrodberaht.injection.extensions.spring;

import org.hrodberaht.injection.ExtendedAnnotationInjection;
import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.InjectionContainerManager;
import org.hrodberaht.injection.config.ContainerConfigBase;
import org.hrodberaht.injection.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.injection.internal.annotation.ReflectionUtils;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.spi.ResourceCreator;
import org.springframework.beans.factory.BeanFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:35:27
 * @version 1.0
 * @since 1.0
 */
public abstract class SpringContainerConfigBase extends ContainerConfigBase<InjectionRegisterScanBase> {

    private boolean hasPersistenceContextInClassPath = true;
    private Map<String, BeanFactory> stringBeanFactoryMap = new HashMap<>();

    private DefaultInjectionPointFinder injectionFinder;

    protected SpringContainerConfigBase(ResourceCreator resourceCreator) {
        this.resourceCreator = resourceCreator;
        initInjectionPoint();
    }

    protected void addSpringConfig(String springConfig) {

    }

    @Override
    protected ResourceCreator createResourceCreator() {
        return new SpringResourceCreator();
    }

    protected void copyOriginalRegistryToActive() {
        activeRegister = originalRegister;
    }


    protected InjectContainer createAutoScanContainerManuallyRunAfterBeanDiscovery(
            RegistrationModuleAnnotation[] moduleAnnotation, String... packageName) {
        InjectionRegisterScanBase registerScan = getScanner(null);
        if (moduleAnnotation != null) {
            ((ExtendedAnnotationInjection) registerScan.getInjectContainer()).getAnnotatedContainer().register(
                    (InjectionContainerManager) registerScan.getInjectContainer(), moduleAnnotation);
        }
        registerScan.scanPackage(packageName);
        originalRegister = registerScan;
        appendTypedResources();
        copyOriginalRegistryToActive();
        System.out.println("createAutoScanContainerManuallyRunAfterBeanDiscovery - " + originalRegister);
        return activeRegister.getContainer();
    }


    protected InjectContainer createAutoScanContainerManuallyRunAfterBeanDiscovery(String... packageName) {
        InjectionRegisterScanBase registerScan = getScanner(null);
        registerScan.scanPackage(packageName);
        originalRegister = registerScan;
        appendTypedResources();
        copyOriginalRegistryToActive();
        System.out.println("createAutoScanContainerManuallyRunAfterBeanDiscovery - " + originalRegister);
        return activeRegister.getContainer();
    }

    protected InjectionRegisterScanBase getScanner(InjectionRegister injectionRegister) {
        return new InjectionRegisterScanSpring(injectionRegister);
    }

    protected SpringContainerConfigBase() {
        initInjectionPoint();
    }

    protected void initInjectionPoint() {
        /*
        this.injectionFinder = new DefaultInjectionPointFinder(this) {
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
        };
        */
    }

    protected void appendTypedResources() {
        originalRegister.register(new RegistrationModuleAnnotation() {
            @Override
            public void registrations() {
                registerInjectionFinder(injectionFinder);
            }
        });

        if (typedResources != null) {
            for (final Class typedResource : typedResources.keySet()) {
                final Object value = typedResources.get(typedResource);
                originalRegister.register(new RegistrationModuleAnnotation() {
                    @Override
                    public void registrations() {
                        register(typedResource).withInstance(value);
                    }
                });
            }
        }
    }

    protected DataSource createDataSource(String dataSourceName) {
        return resourceCreator.createDataSource(dataSourceName);
    }


    public abstract InjectContainer createContainer();

    protected void injectResources(Object serviceInstance) {

        if (resources == null && typedResources == null) {
            return;
        }

        List<Member> members = ReflectionUtils.findMembers(serviceInstance.getClass());
        for (Member member : members) {
            if (member instanceof Field) {
                Field field = (Field) member;
                if (field.isAnnotationPresent(Resource.class)) {
                    Resource resource = field.getAnnotation(Resource.class);
                    if (!injectNamedResource(serviceInstance, field, resource)) {
                        injectTypedResource(serviceInstance, field);
                    }
                }
            }
        }
    }

    public ResourceCreator getResourceCreator() {
        return resourceCreator;
    }

}

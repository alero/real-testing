package org.hrodberaht.injection.extensions.cdi;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.config.jpa.JPAContainerConfigBase;
import org.hrodberaht.injection.extensions.cdi.cdiext.ApplicationCDIExtensions;
import org.hrodberaht.injection.extensions.cdi.cdiext.CDIExtensions;
import org.hrodberaht.injection.extensions.cdi.inner.InjectionRegisterScanCDI;
import org.hrodberaht.injection.extensions.cdi.inner.JSEResourceCreator;
import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.spi.ResourceCreator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public abstract class CDIContainerConfigBase extends JPAContainerConfigBase<InjectionRegisterScanBase> {

    private boolean annotationForEJB = true;

    private CDIExtensions cdiExtensions = createExtensionScanner();
    private DefaultInjectionPointFinder injectionFinder;

    protected CDIContainerConfigBase() {
        initInjectionPoint();
    }

    protected CDIContainerConfigBase(ResourceCreator resourceCreator) {
        this.resourceCreator = resourceCreator;
        initInjectionPoint();
    }

    public abstract InjectContainer createContainer();

    /**
     * Will be removed before the 2.0 final release
     * @param packageName
     * @return the container
     */
    @Deprecated
    protected InjectContainer createAutoScanContainer(String... packageName) {
        InjectionRegisterModule combinedRegister = preScanModuleRegistration();
        cdiExtensions.runBeforeBeanDiscovery(combinedRegister, this);
        createAutoScanContainerRegister(packageName, combinedRegister);
        cdiExtensions.runAfterBeanDiscovery(combinedRegister, this);
        return activeRegister.getContainer();
    }

    public void runBeforeBeanDiscovery(){
        // null as active registry is important
        // its before discovery nothing can be registered in the IoC Container, do not change this
        cdiExtensions.runBeforeBeanDiscovery(null, this);
    }

    public void runAfterBeanDiscovery(){
        cdiExtensions.runAfterBeanDiscovery(activeRegister, this);
    }

    public ResourceCreator getResourceCreator() {
        return resourceCreator;
    }

    protected InjectionRegisterScanBase getScanner(InjectionRegister injectionRegister) {
        return new InjectionRegisterScanCDI(injectionRegister);
    }

    protected CDIExtensions createExtensionScanner() {
        return new ApplicationCDIExtensions();
    }

    @Override
    protected ResourceCreator createResourceCreator() {
        return new JSEResourceCreator();
    }


    protected void initInjectionPoint() {
        this.injectionFinder = new DefaultInjectionPointFinder(this) {
            @Override
            protected boolean hasInjectAnnotationOnMethod(Method method) {
                try {

                    return (annotationForEJB ? method.isAnnotationPresent(EJB.class) : false)
                            || super.hasInjectAnnotationOnMethod(method);
                } catch (NoClassDefFoundError error) {
                    annotationForEJB = false;
                    return super.hasInjectAnnotationOnMethod(method);
                }
            }

            @Override
            protected boolean hasInjectAnnotationOnField(Field field) {
                try {
                    return (annotationForEJB ? field.isAnnotationPresent(EJB.class) : false) ||
                            super.hasInjectAnnotationOnField(field);
                } catch (NoClassDefFoundError error) {
                    annotationForEJB = false;
                    return super.hasInjectAnnotationOnField(field);
                }
            }

            @Override
            protected boolean hasPostConstructAnnotation(Method method) {
                return method.isAnnotationPresent(PostConstruct.class) ||
                        super.hasPostConstructAnnotation(method);
            }

            @Override
            public void extendedInjection(Object service) {
                CDIContainerConfigBase config = (CDIContainerConfigBase) getContainerConfig();
                if (config != null) {
                    config.injectResources(service);
                } else {
                    System.out.println("DefaultInjectionPointFinder NOT injecting resources due to config value null");
                }
            }
        };
    }

    protected void appendTypedResources(InjectionRegisterModule registerModule) {
        originalRegister.register(new RegistrationModuleAnnotation() {
            @Override
            public void registrations() {
                registerInjectionFinder(injectionFinder);
            }
        });

        super.appendTypedResources(originalRegister);
    }

    protected void injectResources(Object serviceInstance) {
        resourceInjection.injectResources(serviceInstance);
    }


}

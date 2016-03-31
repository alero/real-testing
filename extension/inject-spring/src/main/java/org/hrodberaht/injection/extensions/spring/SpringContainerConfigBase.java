package org.hrodberaht.injection.extensions.spring;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.config.jpa.JPAContainerConfigBase;
import org.hrodberaht.injection.internal.ExtendedAnnotationInjection;
import org.hrodberaht.injection.internal.InjectionContainerManager;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.spi.ResourceCreator;
import org.springframework.beans.factory.BeanFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:35:27
 * @version 1.0
 * @since 1.0
 */
public abstract class SpringContainerConfigBase extends JPAContainerConfigBase<InjectionRegisterScanBase> {

    private Map<String, BeanFactory> stringBeanFactoryMap = new HashMap<>();

    protected SpringContainerConfigBase(ResourceCreator resourceCreator) {
        this.resourceCreator = resourceCreator;
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
            ((ExtendedAnnotationInjection) registerScan.getInjectContainer())
                    .getAnnotatedContainer().register(
                        (InjectionContainerManager) registerScan.getInjectContainer(), moduleAnnotation
            );
        }
        registerScan.scanPackage(packageName);
        originalRegister = registerScan;
        appendTypedResources(originalRegister);
        copyOriginalRegistryToActive();
        System.out.println("createAutoScanContainerManuallyRunAfterBeanDiscovery - " + originalRegister);
        return activeRegister.getContainer();
    }


    protected InjectContainer createAutoScanContainerManuallyRunAfterBeanDiscovery(String... packageName) {
        InjectionRegisterScanBase registerScan = getScanner(null);
        registerScan.scanPackage(packageName);
        originalRegister = registerScan;
        appendTypedResources(originalRegister);
        copyOriginalRegistryToActive();
        System.out.println("createAutoScanContainerManuallyRunAfterBeanDiscovery - " + originalRegister);
        return activeRegister.getContainer();
    }

    protected InjectionRegisterScanBase getScanner(InjectionRegister injectionRegister) {
        return new InjectionRegisterScanSpring(injectionRegister);
    }

    protected SpringContainerConfigBase() {

    }

    public abstract InjectContainer createContainer();


    public ResourceCreator getResourceCreator() {
        return resourceCreator;
    }

}

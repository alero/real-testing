package org.hrodberaht.inject.register;

import org.hrodberaht.inject.ClassScanner;
import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.InjectionContainerManager;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-jun-03 17:49:42
 * @version 1.0
 * @since 1.0
 */
public abstract class RegistrationModuleAnnotationScanner extends RegistrationModuleAnnotation {


    private InjectionContainerManager injectionContainerManager = null;

    public RegistrationModuleAnnotationScanner scanAndRegister(String... packages) {
        ClassScanner classScanner = new ClassScanner();
        for (String packagename : packages) {
            Class[] clazzs = classScanner.getClasses(packagename);
            for (Class aClazz : clazzs) {
                classScanner.createRegistration(aClazz, getInjectionContainerManager());
            }
        }
        return this;
    }

    public abstract void scan();

    @Override
    public void postRegistration(InjectContainer injectContainer) {
        scan();
    }

    @Override
    public void registrations() {
    }

    public void setInjectionContainerManager(InjectionContainerManager injectionContainerManager) {
        this.injectionContainerManager = injectionContainerManager;
    }

    private InjectionContainerManager getInjectionContainerManager() {
        return injectionContainerManager;
    }
}

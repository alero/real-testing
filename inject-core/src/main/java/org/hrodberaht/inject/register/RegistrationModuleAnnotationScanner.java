package org.hrodberaht.inject.register;

import org.hrodberaht.inject.ClassScanner;
import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.SimpleInjection;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-jun-03 17:49:42
 * @version 1.0
 * @since 1.0
 */
public abstract class RegistrationModuleAnnotationScanner extends RegistrationModuleAnnotation {


    private SimpleInjection simpleInjection = null;

    public RegistrationModuleAnnotationScanner scanAndRegister(String... packages) {
        ClassScanner classScanner = new ClassScanner();
        for (String packagename : packages) {
            Class[] clazzs = classScanner.getClasses(packagename);
            for (Class aClazz : clazzs) {
                classScanner.createRegistration(aClazz, getSimpleInjection());
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

    public void setSimpleInjection(SimpleInjection simpleInjection) {
        this.simpleInjection = simpleInjection;
    }

    private SimpleInjection getSimpleInjection() {
        return simpleInjection;
    }
}

package org.hrodberaht.inject.register;

import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.InjectionRegisterScan;
import org.hrodberaht.inject.internal.annotation.InjectionMetaDataBase;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-jun-03 17:49:42
 * @version 1.0
 * @since 1.0
 */
public abstract class RegistrationModuleAnnotationScanner extends RegistrationModuleAnnotation {



    public abstract void scan();

    @Override
    public void preRegistration(InjectContainer injectContainer) {
        scan();
    }

    @Override
    public void registrations() {
    }

    public void scanAndRegister(String... packages) {
        InjectionRegisterScan injectionRegisterScan = new InjectionRegisterScan();
        injectionRegisterScan.scanPackage(packages);
        mergeWith(injectionRegisterScan, this);
    }

    private void mergeWith(InjectionRegisterScan injectionRegisterScan, RegistrationModuleAnnotationScanner registrationModuleAnnotationScanner) {
        for(InjectionMetaDataBase serviceMetaData:injectionRegisterScan.getInnerContainer().getAnnotatedContainer().getInjectionMetaDataBaseCollection()){
            registrationModuleAnnotationScanner.register(
                    serviceMetaData.getServiceRegister().getService(),
                    serviceMetaData.getInjectionMetaData().getServiceClass());
        }
    }


}

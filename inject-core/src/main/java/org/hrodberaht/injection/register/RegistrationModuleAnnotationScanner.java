package org.hrodberaht.injection.register;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.internal.InjectionRegisterScan;
import org.hrodberaht.injection.internal.annotation.InjectionMetaDataBase;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-jun-03 17:49:42
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
        InjectionRegisterScanBase injectionRegisterScan = getScanner();
        injectionRegisterScan.scanPackage(packages);
        mergeWith(injectionRegisterScan, this);
    }

    public InjectionRegisterScanBase getScanner() {
        return new InjectionRegisterScan();
    }

    private void mergeWith(InjectionRegisterScanBase injectionRegisterScan, RegistrationModuleAnnotationScanner registrationModuleAnnotationScanner) {
        for (InjectionMetaDataBase serviceMetaData : injectionRegisterScan.getInnerContainer().getAnnotatedContainer().getInjectionMetaDataBaseCollection()) {
            registrationModuleAnnotationScanner.register(
                    serviceMetaData.getServiceRegister().getService(),
                    serviceMetaData.getInjectionMetaData().getServiceClass());
        }
    }


}

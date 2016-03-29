package org.hrodberaht.injection.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.InjectionContainerManager;
import org.hrodberaht.injection.InjectionRegisterModule;
import org.hrodberaht.injection.ScopeContainer;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.spi.InjectionRegisterScanInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Injection Extension JUnit
 *
 * @author Robert Alexandersson
 *         2010-okt-26 19:09:57
 * @version 1.0
 * @since 1.0
 */
public abstract class InjectionRegisterScanBase<T extends InjectionRegisterScanBase> extends InjectionRegisterModule implements InjectionRegisterScanInterface, ScanningService {

    private static final Logger LOG = LoggerFactory.getLogger(InjectionRegisterScanBase.class);

    protected InjectionRegister referedRegister;

    public InjectionRegisterScanBase() {
    }

    public InjectionRegisterScanBase(InjectionRegister register) {
        super(register);
        referedRegister = register;
    }


    private ClassScanner classScanner = new ClassScanner();

    public abstract T clone();

    public void setInjectContainer(InjectContainer injectContainer) {
        super.container = (InjectionContainerManager) injectContainer;
    }

    @Override
    public InjectContainer getInjectContainer() {
        return container;
    }

    public T scanPackage(String... packagenames) {
        for (String packagename : packagenames) {

            List<Class> listOfClasses = classScanner.getClasses(packagename);
            for (Class aClazz : listOfClasses) {
                ClassScanningUtil.createRegistration(aClazz, listOfClasses, this);
            }
        }
        return (T) this;
    }

    public T scanPackageExclude(String packagename, Class... manuallyexcluded) {
        List<Class> listOfClasses = classScanner.getClasses(packagename);
        List<Class> listOfFilteredClasses = new ArrayList<>(listOfClasses.size()-manuallyexcluded.length);
        // remove the manual excludes
        for (Class aClazz : listOfClasses) {
            if (!manuallyExcluded(aClazz, manuallyexcluded)) {
                listOfFilteredClasses.add(aClazz);
            }
        }
        for (Class aClazz : listOfFilteredClasses) {
            ClassScanningUtil.createRegistration(aClazz, listOfFilteredClasses, this);
        }
        return (T) this;
    }

    public void registerForScanner(Class aClazz, Class serviceClass, ScopeContainer.Scope scope){
        RegistrationModuleAnnotation registrationModule = new RegistrationModuleAnnotation() {
            @Override
            public void registrations() {
                register(aClazz).scopeAs(scope).with(serviceClass);
            }
        };
        container.register(registrationModule);
    }


    private boolean manuallyExcluded(Class aClazz, Class[] manuallyexluded) {
        for (Class excluded : manuallyexluded) {
            if (excluded == aClazz) {
                return true;
            }
        }
        return false;
    }


    public void overrideRegister(final Class serviceDefinition, final Object service) {
        RegistrationModuleAnnotation registrationModule = new RegistrationModuleAnnotation() {
            @Override
            public void registrations() {
                register(serviceDefinition).withInstance(service);
            }
        };
        container.register(registrationModule);
    }



}

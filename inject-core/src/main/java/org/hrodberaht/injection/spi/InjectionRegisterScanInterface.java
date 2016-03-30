package org.hrodberaht.injection.spi;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.register.RegistrationModule;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-11-28
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
public interface InjectionRegisterScanInterface {
    InjectionRegisterScanInterface scanPackage(String... packagenames);

    InjectionRegisterScanInterface scanPackageExclude(String packagename, Class... manuallyExcluded);

    void overrideRegister(Class serviceDefinition, Object service);

    InjectionRegisterModule register(RegistrationModule... modules);

    InjectContainer getInjectContainer();

    void setInjectContainer(InjectContainer injectContainer);

    InjectionRegisterScanInterface clone();


}

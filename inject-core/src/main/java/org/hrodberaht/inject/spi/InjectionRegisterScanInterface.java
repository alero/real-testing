package org.hrodberaht.inject.spi;

import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.InjectionRegisterModule;
import org.hrodberaht.inject.register.RegistrationModule;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-11-28
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
public interface InjectionRegisterScanInterface {
    InjectionRegisterScanInterface scanPackage(String... packagenames);

    InjectionRegisterScanInterface scanPackage(String packagename, Class... manuallyexcluded);

    void overrideRegister(Class serviceDefinition, Object service);

    InjectionRegisterModule register(RegistrationModule... modules);

    InjectContainer getInjectContainer();

    void setInjectContainer(InjectContainer injectContainer);

    InjectionRegisterScanInterface clone();


}

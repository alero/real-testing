package org.hrodberaht.injection.plugin.junit.spi;

import org.hrodberaht.injection.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.spi.ContainerConfigBuilder;

public interface InjectionPlugin extends Plugin {

    void setInjectionRegister(InjectionRegister injectionRegister);

    InjectionFinder getInjectionFinder(ContainerConfigBuilder containerConfigBuilder);
}

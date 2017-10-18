package org.hrodberaht.injection.plugin.junit.spi;

import org.hrodberaht.injection.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.spi.ContainerConfigBuilder;

public interface InjectionPlugin extends Plugin {

    void setInjectionRegister(InjectionRegister containerConfigBuilder);
    DefaultInjectionPointFinder getInjectionFinder(ContainerConfigBuilder containerConfigBuilder);
}

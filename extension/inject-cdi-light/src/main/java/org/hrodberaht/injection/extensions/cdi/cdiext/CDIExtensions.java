package org.hrodberaht.injection.extensions.cdi.cdiext;

import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.spi.ContainerConfig;

/**
 * Created by alexbrob on 2016-03-31.
 */
public interface CDIExtensions {
    void runAfterBeanDiscovery(InjectionRegister register, ContainerConfig containerConfig);

    void runBeforeBeanDiscovery(InjectionRegister register, ContainerConfig containerConfig);
}

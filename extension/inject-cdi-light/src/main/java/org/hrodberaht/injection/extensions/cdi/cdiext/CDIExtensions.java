package org.hrodberaht.injection.extensions.cdi.cdiext;

import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.spi.ContainerConfig;

/**
 * Created by alexbrob on 2016-03-31.
 */
public interface CDIExtensions {
    void runAfterBeanDiscovery(InjectionRegisterModule register, ContainerConfig containerConfig);
    void runBeforeBeanDiscovery(InjectionRegisterModule register, ContainerConfig containerConfig);
}

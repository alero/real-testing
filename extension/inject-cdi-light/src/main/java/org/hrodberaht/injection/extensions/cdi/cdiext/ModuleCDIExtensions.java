package org.hrodberaht.injection.extensions.cdi.cdiext;

import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.spi.ContainerConfig;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-12-03
 * Time: 12:29
 * To change this template use File | Settings | File Templates.
 */
public class ModuleCDIExtensions implements CDIExtensions{

    public ModuleCDIExtensions() {
    }

    public void runAfterBeanDiscovery(InjectionRegisterModule register, ContainerConfig containerConfig) {
        throw new RuntimeException("not yet implemented");
    }

    public void runBeforeBeanDiscovery(InjectionRegisterModule register, ContainerConfig containerConfig) {
        throw new RuntimeException("not yet implemented");
    }




}

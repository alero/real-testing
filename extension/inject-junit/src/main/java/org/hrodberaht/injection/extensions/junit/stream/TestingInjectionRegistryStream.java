package org.hrodberaht.injection.extensions.junit.stream;

import org.hrodberaht.injection.config.ContainerConfigBase;
import org.hrodberaht.injection.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.extensions.junit.ejb.internal.InjectionRegisterScanEJB;
import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.stream.InjectionRegistryStream;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class TestingInjectionRegistryStream extends InjectionRegistryStream {

    private ContainerConfigBase configBase;

    public TestingInjectionRegistryStream(ContainerConfigBase configBase) {
        this.configBase = configBase;
    }

    @Override
    public InjectionRegisterScanBase getCustomScanner() {
        if(configBase.getActiveRegister() != null) {
            InjectionRegisterModule injectionRegisterModule = new InjectionRegisterModule(configBase.getActiveRegister());
            return new InjectionRegisterScanEJB(injectionRegisterModule);
        }
        return new InjectionRegisterScanEJB(new InjectionRegisterModule());
    }


}

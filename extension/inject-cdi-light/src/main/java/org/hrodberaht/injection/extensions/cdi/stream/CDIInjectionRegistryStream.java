package org.hrodberaht.injection.extensions.cdi.stream;

import org.hrodberaht.injection.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.config.jpa.JPAContainerConfigBase;
import org.hrodberaht.injection.extensions.cdi.CDIModule;
import org.hrodberaht.injection.extensions.cdi.inner.InjectionRegisterScanCDI;
import org.hrodberaht.injection.stream.AppendModuleFunc;
import org.hrodberaht.injection.stream.InjectionRegistryStream;
import org.hrodberaht.injection.stream.RegisterModuleFunc;
import org.hrodberaht.injection.stream.RegisterResourceFunc;
import org.hrodberaht.injection.stream.ScanModuleFunc;

/**
 * Created by alexbrob on 2016-03-31.
 */
public class CDIInjectionRegistryStream<T extends CDIModule> extends InjectionRegistryStream<T> {

    public CDIInjectionRegistryStream(JPAContainerConfigBase configBase) {
        super(configBase);
    }

    @Override
    protected T createModuleContainer() {
        return (T) new CDIModule(getContainer());
    }

    @Override
    public CDIInjectionRegistryStream scan(ScanModuleFunc scanModuleFunc) {
        super.scan(scanModuleFunc);
        return this;
    }

    @Override
    public CDIInjectionRegistryStream module(AppendModuleFunc scanModuleFunc) {
        super.module(scanModuleFunc);
        return this;
    }

    @Override
    public CDIInjectionRegistryStream register(RegisterModuleFunc scanModuleFunc) {
        super.register(scanModuleFunc);
        return this;
    }

    public CDIInjectionRegistryStream resource(RegisterResourceFunc registerResourceFunc) {
        super.resource(registerResourceFunc);
        return this;
    }

    @Override
    public InjectionRegisterScanBase getCustomScanner() {
        return new InjectionRegisterScanCDI();
    }

    @Override
    public T getModule() {
        return super.getModule();
    }


}

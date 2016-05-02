package org.hrodberaht.injection.extensions.spring.stream;

import org.hrodberaht.injection.config.jpa.JPAContainerConfigBase;
import org.hrodberaht.injection.extensions.spring.SpringModule;
import org.hrodberaht.injection.stream.InjectionRegistryStream;
import org.hrodberaht.injection.stream.RegisterModuleFunc;
import org.hrodberaht.injection.stream.RegisterResourceFunc;
import org.hrodberaht.injection.stream.ScanModuleFunc;

/**
 * Created by alexbrob on 2016-03-31.
 */
public class SpringInjectionRegistryStream<T extends SpringModule> extends InjectionRegistryStream<T> {

    public SpringInjectionRegistryStream(JPAContainerConfigBase configBase) {
        super(configBase);
    }

    private Class[] springConfigs = null;

    @Override
    protected T createModuleContainer() {
        return (T) new SpringModule(getContainer());
    }

    @Override
    public SpringInjectionRegistryStream scan(ScanModuleFunc scanModuleFunc) {
        super.scan(scanModuleFunc);
        return this;
    }

    @Override
    public SpringInjectionRegistryStream register(RegisterModuleFunc scanModuleFunc) {
        super.register(scanModuleFunc);
        return this;
    }

    @Override
    public SpringInjectionRegistryStream resource(RegisterResourceFunc registerResourceFunc) {
        super.resource(registerResourceFunc);
        return this;
    }

    public SpringInjectionRegistryStream springConfig(SpringModuleFunc scanModuleFunc) {
        springConfigs = new Class[]{scanModuleFunc.value()};
        return this;
    }

    public SpringInjectionRegistryStream springConfig(SpringModulesFunc scanModuleFunc) {
        ConfigResource configResource = new ConfigResource();
        springConfigs = scanModuleFunc.value(configResource);
        return this;
    }


    @Override
    public T getModule() {
        T module = super.getModule();
        module.setClasses(springConfigs);
        return module;
    }


}

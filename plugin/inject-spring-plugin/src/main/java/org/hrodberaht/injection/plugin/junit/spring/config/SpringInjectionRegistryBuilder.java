package org.hrodberaht.injection.plugin.junit.spring.config;

import org.hrodberaht.injection.config.ContainerConfig;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;


public class SpringInjectionRegistryBuilder<T extends SpringModule> extends InjectionRegistryBuilder<T> {

    public SpringInjectionRegistryBuilder(ContainerConfig configBase) {
        super(configBase);
    }

    private Class[] springConfigs = null;

    @Override
    protected T createModuleContainer() {
        return (T) new SpringModule(getContainer());
    }

    public SpringInjectionRegistryBuilder springConfig(SpringModuleFunc scanModuleFunc) {
        springConfigs = new Class[]{scanModuleFunc.value()};
        return this;
    }

    public SpringInjectionRegistryBuilder springConfig(SpringModulesFunc scanModuleFunc) {
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

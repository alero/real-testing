package org.hrodberaht.injection.plugin.junit.spring.plugins;

import org.hrodberaht.injection.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.injection.plugin.junit.spi.InjectionPlugin;
import org.hrodberaht.injection.plugin.junit.spi.Plugin;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.spi.ContainerConfigBuilder;

/**
 * The SpringInjectionPlugin is intended to not "start" a spring container but to
 * emulate the boot sequence of spring and move the beans found to the hrodberaht IoC container
 * @see org.hrodberaht.injection.internal.InjectionContainer
 */
public class SpringInjectionPlugin implements InjectionPlugin {

    public SpringInjectionPlugin() {
        throw new RuntimeException("Not yet supported");
    }

    @Override
    public void setInjectionRegister(InjectionRegister containerConfigBuilder) {

    }

    @Override
    public DefaultInjectionPointFinder getInjectionFinder(ContainerConfigBuilder containerConfigBuilder) {
        return null;
    }

    @Override
    public LifeCycle getLifeCycle() {
        return null;
    }
}

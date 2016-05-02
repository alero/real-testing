package org.hrodberaht.injection.extensions.spring.junit;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.spring.stream.SpringStreamApplication;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.spi.ContainerConfig;
import org.hrodberaht.injection.spi.ResourceCreator;

public class JunitSpringStreamApplication extends SpringStreamApplication implements ContainerConfig {

    private JUnitSpringContainerConfigBase configBase;
    private JUnitSpringInjectionRegistryStream stream;

    @Override
    protected JUnitSpringContainerConfigBase createSpringContainerConfigBase() {
        configBase = new JUnitSpringContainerConfigBase(this);
        return configBase;
    }

    protected JUnitSpringInjectionRegistryStream stream() {
        if (stream != null) {
            throw new IllegalAccessError("Only one stream per config is allowed");
        }
        stream = new JUnitSpringInjectionRegistryStream(configBase);
        springApplication = createSpringApplication();
        return stream;
    }


    @Override
    public void cleanActiveContainer() {
        configBase.cleanActiveContainer();
    }

    @Override
    public InjectionRegister getActiveRegister() {
        return configBase.getActiveRegister();
    }

    @Override
    public InjectContainer getActiveContainer() {
        return configBase.getActiveContainer();
    }

    @Override
    public void addSingletonActiveRegistry() {
        configBase.addSingletonActiveRegistry();
    }

    @Override
    public ResourceCreator getResourceCreator() {
        return configBase.getResourceCreator();
    }

    @Override
    public InjectContainer createContainer() {
        getSpringApplication().add(stream.getModule());
        return getSpringApplication().createContainer();
    }
}

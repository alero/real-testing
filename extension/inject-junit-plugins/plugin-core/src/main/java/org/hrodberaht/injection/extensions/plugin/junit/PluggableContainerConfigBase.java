package org.hrodberaht.injection.extensions.plugin.junit;

import org.hrodberaht.injection.config.ContainerConfig;
import org.hrodberaht.injection.extensions.plugin.junit.resources.PluggableResourceFactory;
import org.hrodberaht.injection.internal.ResourceInject;
import org.hrodberaht.injection.internal.ResourceInjection;
import org.hrodberaht.injection.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.spi.ResourceFactory;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class PluggableContainerConfigBase extends ContainerConfig {

    @Override
    protected ResourceFactory createResourceFactory() {
        return new PluggableResourceFactory();
    }

    @Override
    protected ResourceInject createResourceInject() {
        return new ResourceInject();
    }

    @Override
    public void injectResources(Object serviceInstance) {
        PluggableResourceFactory pluggableResourceFactory = (PluggableResourceFactory)resourceFactory;
        resourceInjection.injectResources(pluggableResourceFactory.getTypedMap(), pluggableResourceFactory.getNamedMap(), serviceInstance);
    }

    @Override
    protected void appendResources(InjectionRegister registerModule) {
        PluggableResourceFactory pluggableResourceFactory = (PluggableResourceFactory)resourceFactory;

        pluggableResourceFactory.getNamedMap().forEach((resourceKey, value) -> {
            registerModule.register(new RegistrationModuleAnnotation() {
                @Override
                public void registrations() {
                    register(resourceKey.getType()).named(resourceKey.getName()).withInstance(value);
                }
            });
        });

        pluggableResourceFactory.getTypedMap().forEach((aClass, value) -> {
            registerModule.register(new RegistrationModuleAnnotation() {
                @Override
                public void registrations() {
                    register(aClass).withInstance(value);
                }
            });
        });


    }




}

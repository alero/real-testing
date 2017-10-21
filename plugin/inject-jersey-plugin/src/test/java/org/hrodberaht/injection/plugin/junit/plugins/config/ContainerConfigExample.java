package org.hrodberaht.injection.plugin.junit.plugins.config;

import org.hrodberaht.injection.plugin.junit.PluggableContainerConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.JerseyPlugin;
import org.hrodberaht.injection.plugin.junit.plugins.service.JerseyApplication;
import org.hrodberaht.injection.plugin.junit.plugins.service.ObjectMapperResolver;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

public class ContainerConfigExample extends PluggableContainerConfigBase {

    @Override
    protected void register(InjectionRegistryBuilder registryBuilder) {
        activatePlugin(JerseyPlugin.class).build()
                .clientConfig(config -> config.register(ObjectMapperResolver.class))
                .resourceConfig(JerseyApplication::new);
    }
}

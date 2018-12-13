package org.hrodberaht.injection.plugin.junit.plugins.tests;

import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.plugins.ActiveMQPlugin;
import org.hrodberaht.injection.plugin.junit.plugins.SpringExtensionPlugin;
import org.hrodberaht.injection.plugin.junit.plugins.tests.service.ConsumerConfig;
import org.hrodberaht.injection.plugin.junit.plugins.tests.service.ProducerConfig;

public class TestContextConfig extends ContainerContextConfigBase {
    @Override
    public void register(InjectionRegistryBuilder registryBuilder) {
        ActiveMQPlugin activatePlugin = activatePlugin(new ActiveMQPlugin(Plugin.LifeCycle.TEST_SUITE));

        activatePlugin(SpringExtensionPlugin.class)
                .with(activatePlugin)
                .springConfig(ProducerConfig.class, ConsumerConfig.class)
        ;
    }
}

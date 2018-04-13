package org.hrodberaht.injection.plugin.junit.plugins.config;

import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.plugins.GuiceExtensionPlugin;
import org.hrodberaht.injection.plugin.junit.plugins.test.service.config.GuiceModule;

public class JUnitPluginConfig extends ContainerContextConfigBase {
    @Override
    public void register(InjectionRegistryBuilder registryBuilder) {


        activatePlugin(new GuiceExtensionPlugin(Plugin.LifeCycle.TEST_CLASS)).guiceModules(new GuiceModule());
    }
}

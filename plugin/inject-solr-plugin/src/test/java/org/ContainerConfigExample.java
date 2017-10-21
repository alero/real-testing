package org;

import org.hrodberaht.injection.plugin.junit.PluggableContainerConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.SolrJPlugin;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

public class ContainerConfigExample extends PluggableContainerConfigBase {
    @Override
    protected void register(InjectionRegistryBuilder registryBuilder) {

        activatePlugin(SolrJPlugin.class).loadCollection("collection1");

    }
}

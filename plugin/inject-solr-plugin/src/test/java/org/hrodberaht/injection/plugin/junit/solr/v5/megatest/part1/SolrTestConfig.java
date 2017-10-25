package org.hrodberaht.injection.plugin.junit.solr.v5.megatest.part1;

import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.plugins.SolrJPlugin;

public class SolrTestConfig extends ContainerContextConfigBase {
    @Override
    public void register(InjectionRegistryBuilder registryBuilder) {
        activatePlugin(SolrJPlugin.class)
                .lifeCycle(Plugin.ResourceLifeCycle.TEST_CONFIG)
                .coreName("collection1");
    }
}

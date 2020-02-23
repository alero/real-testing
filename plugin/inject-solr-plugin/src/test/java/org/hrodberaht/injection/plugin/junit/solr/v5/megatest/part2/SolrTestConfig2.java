package org.hrodberaht.injection.plugin.junit.solr.v5.megatest.part2;

import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.plugins.SolrJPlugin;

public class SolrTestConfig2 extends ContainerContextConfigBase {
    @Override
    public void register(InjectionRegistryBuilder registryBuilder) {
        activatePlugin(new SolrJPlugin()
                .lifeCycle(Plugin.LifeCycle.TEST_CONFIG)
                .coreName("collection1")
        );
    }
}

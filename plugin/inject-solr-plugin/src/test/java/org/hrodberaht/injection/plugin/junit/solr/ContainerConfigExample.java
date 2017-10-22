package org.hrodberaht.injection.plugin.junit.solr;

import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.SolrJPlugin;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

public class ContainerConfigExample extends ContainerContextConfigBase {
    @Override
    protected void register(InjectionRegistryBuilder registryBuilder) {

        activatePlugin(SolrJPlugin.class).loadCollection("collection1");

    }
}

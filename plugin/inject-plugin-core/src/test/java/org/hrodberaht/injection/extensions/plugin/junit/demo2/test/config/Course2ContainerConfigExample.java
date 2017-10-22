package org.hrodberaht.injection.extensions.plugin.junit.demo2.test.config;

import org.hrodberaht.injection.extensions.plugin.junit.demo2.service.MyResource;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.ResourcePlugin;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 * 2011-05-03 20:31
 * @created 1.0
 * @since 1.0
 */
public class Course2ContainerConfigExample extends ContainerContextConfigBase {

    @Override
    public void register(InjectionRegistryBuilder registryBuilder) {
        activatePlugin(ResourcePlugin.class);
        registryBuilder
                .scan(() -> "org.hrodberaht.injection.extensions.plugin.junit.demo2.service")
                .resource(builder ->
                        builder
                                .resource("myResource", new MyResource("named"))
                                .resource("myMappedResource", new MyResource("mapped"))
                                .resource(new MyResource("typed"))

                )
        ;
    }
}

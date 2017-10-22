package org.hrodberaht.injection.plugin.junit;

import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

public interface ContainerContextConfig {
    void register(InjectionRegistryBuilder registryBuilder);
}

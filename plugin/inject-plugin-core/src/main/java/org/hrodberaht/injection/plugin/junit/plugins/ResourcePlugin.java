package org.hrodberaht.injection.plugin.junit.plugins;

import org.hrodberaht.injection.plugin.junit.resources.ResourcePluginBase;
import org.hrodberaht.injection.plugin.junit.spi.Plugin;

public class ResourcePlugin extends ResourcePluginBase implements Plugin {

    @Override
    public LifeCycle getLifeCycle() {
        return LifeCycle.TEST_SUITE;
    }
}

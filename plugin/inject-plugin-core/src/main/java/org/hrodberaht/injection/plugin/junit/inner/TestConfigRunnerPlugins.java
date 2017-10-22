package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.plugin.junit.spi.Plugin;

import java.util.HashMap;
import java.util.Map;

public class TestConfigRunnerPlugins extends PluginRunnerBase {

    public TestConfigRunnerPlugins(Map<Class<? extends Plugin>, Plugin> activePlugins) {
        super(activePlugins, new HashMap<>(), new AnnotatedRunnerPlugin());
    }
}

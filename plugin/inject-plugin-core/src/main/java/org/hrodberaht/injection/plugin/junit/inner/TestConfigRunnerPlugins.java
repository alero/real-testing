package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.plugin.junit.spi.Plugin;
import org.hrodberaht.injection.plugin.junit.spi.RunnerPlugin;
import org.hrodberaht.injection.register.InjectionRegister;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class TestConfigRunnerPlugins extends PluginRunnerBase {

    public TestConfigRunnerPlugins(Map<Class<? extends Plugin>, Plugin> activePlugins) {
        super(activePlugins, new HashMap<>(), new AnnotatedRunnerPlugin());
    }
}

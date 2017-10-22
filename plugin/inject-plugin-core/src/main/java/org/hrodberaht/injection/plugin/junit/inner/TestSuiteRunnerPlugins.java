package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.plugin.junit.spi.Plugin;
import org.hrodberaht.injection.plugin.junit.spi.RunnerPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestSuiteRunnerPlugins extends PluginRunnerBase {

    private static final Map<Class, RunnerPlugin> runnerPlugins = new ConcurrentHashMap<>();
    private static final AnnotatedRunnerPlugin annotatedPluginRunner = new AnnotatedRunnerPlugin();


    public TestSuiteRunnerPlugins(Map<Class<? extends Plugin>, Plugin> activePlugins) {
        super(activePlugins, runnerPlugins, annotatedPluginRunner);
    }
}

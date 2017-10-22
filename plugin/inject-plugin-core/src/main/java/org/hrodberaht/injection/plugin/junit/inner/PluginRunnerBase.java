package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.plugin.junit.spi.Plugin;
import org.hrodberaht.injection.plugin.junit.spi.RunnerPlugin;
import org.hrodberaht.injection.register.InjectionRegister;

import java.lang.annotation.Annotation;
import java.util.Map;

public abstract class PluginRunnerBase implements RunnerPluginInterface {

    private final Map<Class, RunnerPlugin> runnerPlugins;
    private final AnnotatedRunnerPlugin annotatedPluginRunner;
    private final Map<Class<? extends Plugin>, Plugin> activePlugins;

    public PluginRunnerBase(Map<Class<? extends Plugin>, Plugin> activePlugins,
                            Map<Class, RunnerPlugin> runnerPlugins,
                            AnnotatedRunnerPlugin annotatedPluginRunner) {
        this.activePlugins = activePlugins;
        this.runnerPlugins = runnerPlugins;
        this.annotatedPluginRunner = annotatedPluginRunner;
    }

    public RunnerPlugin addPlugin(RunnerPlugin runnerPlugin) {
        if (runnerPlugins.get(runnerPlugin.getClass()) != null) {
            return runnerPlugins.get(runnerPlugin.getClass());
        }
        runnerPlugins.put(runnerPlugin.getClass(), runnerPlugin);
        return runnerPlugin;
    }

    @Override
    public Plugin addAnnotatedPlugin(Plugin plugin) {
        return annotatedPluginRunner.addPlugin(plugin);
    }

    public void runInitBeforeContainer() {
        runnerPlugins.forEach((aClass, runnerPlugin) -> runIfActive(aClass, runnerPlugin::beforeContainerCreation));
    }


    interface RunnerPluginOp {
        void runOp();
    }

    void runIfActive(Class pluginClass, RunnerPluginOp runnerPluginOp) {
        if (activePlugins.get(pluginClass) != null) {
            runnerPluginOp.runOp();
        }
    }

    public void runInitAfterContainer(InjectionRegister injectionRegister) {
        runnerPlugins.forEach((aClass, runnerPlugin) -> runIfActive(aClass, () -> runnerPlugin.afterContainerCreation(injectionRegister)));
    }

    @Override
    public void findAnnotationAndInvokeMethod(InjectionRegister injectionRegister, Class<Annotation> annotation) {
        annotatedPluginRunner.findAnnotationAndInvokeMethod(this, injectionRegister, annotation);
    }

    public void runBeforeTest(InjectionRegister injectionRegister) {
        runnerPlugins.forEach((aClass, runnerPlugin) -> runIfActive(aClass, () -> runnerPlugin.beforeTest(injectionRegister)));
    }

    public void runAfterTest(InjectionRegister injectionRegister) {
        runnerPlugins.forEach((aClass, runnerPlugin) -> runIfActive(aClass, () -> runnerPlugin.afterTest(injectionRegister)));
    }

    public void runBeforeTestClass(InjectionRegister injectionRegister) {
        runnerPlugins.forEach((aClass, runnerPlugin) -> runIfActive(aClass, () -> runnerPlugin.beforeTestClass(injectionRegister)));
    }

    public void runAfterTestClass(InjectionRegister injectionRegister) {
        runnerPlugins.forEach((aClass, runnerPlugin) -> runIfActive(aClass, () -> runnerPlugin.afterTestClass(injectionRegister)));
    }
}

package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.plugin.junit.spi.RunnerPlugin;
import org.hrodberaht.injection.register.InjectionRegister;

import java.util.HashMap;
import java.util.Map;

public class TestRunnerPlugins implements RunnerPluginInterface {


    private Map<Class, RunnerPlugin> runnerPlugins = new HashMap<>();

    public RunnerPlugin addPlugin(RunnerPlugin runnerPlugin) {
        if (runnerPlugins.get(runnerPlugin.getClass()) != null) {
            throw new InjectRuntimeException("Can not register same RunnerPlugin several times");
        }
        runnerPlugins.put(runnerPlugin.getClass(), runnerPlugin);
        return runnerPlugin;
    }

    public void runInitBeforeContainer() {
        runnerPlugins.forEach((aClass, runnerPlugin) -> runnerPlugin.beforeContainerCreation());
    }

    public void runInitAfterContainer(InjectionRegister injectionRegister) {
        runnerPlugins.forEach((aClass, runnerPlugin) -> runnerPlugin.afterContainerCreation(injectionRegister));
    }

    public void runBeforeTest(InjectionRegister injectionRegister) {
        runnerPlugins.forEach((aClass, runnerPlugin) -> runnerPlugin.beforeTest(injectionRegister));
    }

    public void runAfterTest(InjectionRegister injectionRegister) {
        runnerPlugins.forEach((aClass, runnerPlugin) -> runnerPlugin.afterTest(injectionRegister));
    }

    public void runBeforeTestClass(InjectionRegister injectionRegister) {
        runnerPlugins.forEach((aClass, runnerPlugin) -> runnerPlugin.beforeTestClass(injectionRegister));
    }

    public void runAfterTestClass(InjectionRegister injectionRegister) {
        runnerPlugins.forEach((aClass, runnerPlugin) -> runnerPlugin.afterTestClass(injectionRegister));
    }
}

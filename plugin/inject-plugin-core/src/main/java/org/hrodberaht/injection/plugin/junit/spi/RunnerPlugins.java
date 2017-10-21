package org.hrodberaht.injection.plugin.junit.spi;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.register.InjectionRegister;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class RunnerPlugins {


    private Map<Class, RunnerPlugin> runnerPlugins = new HashMap<>();

    public void addPlugin(RunnerPlugin runnerPlugin) {
        if (runnerPlugins.get(runnerPlugin.getClass()) != null) {
            throw new InjectRuntimeException("Can not register same RunnerPlugin several times");
        }
        runnerPlugins.put(runnerPlugin.getClass(), runnerPlugin);
    }

    public void runInitBeforeContainer() {
        runnerPlugins.forEach((aClass, runnerPlugin) -> runnerPlugin.beforeContainerCreation());
    }

    public void runInitAfterContainer(InjectionRegister injectionRegister) {
        runnerPlugins.forEach((aClass, runnerPlugin) -> runnerPlugin.afterContainerCreation(injectionRegister));
    }

    public void runBeforeTest(InjectionRegister injectionRegister) {
        runnerPlugins.forEach((aClass, runnerPlugin) -> runnerPlugin.beforeMethod(injectionRegister));
    }

    public void runAfterTest(InjectionRegister injectionRegister) {
        runnerPlugins.forEach((aClass, runnerPlugin) -> runnerPlugin.afterMethod(injectionRegister));
    }
}

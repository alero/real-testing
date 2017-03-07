package org.hrodberaht.injection.extensions.junit.spi;

import org.hrodberaht.injection.internal.exception.InjectRuntimeException;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class RunnerPlugins {

    private Map<Class, RunnerPlugin> runnerPlugins = new HashMap<>();

    public void addPlugin(RunnerPlugin runnerPlugin){
        if(runnerPlugins.get(runnerPlugin.getClass()) != null){
            throw new InjectRuntimeException("Can not register plugin several times");
        }
        runnerPlugins.put(runnerPlugin.getClass(), runnerPlugin);
    }

    public void runInitBeforeContainer(){
        runnerPlugins.forEach((aClass, runnerPlugin) -> runnerPlugin.beforeContainerCreation());
    }

    public void runInitAfterContainer() {
        runnerPlugins.forEach((aClass, runnerPlugin) -> runnerPlugin.afterContainerCreation());
    }

    public void runBeforeTest(){
        runnerPlugins.forEach((aClass, runnerPlugin) -> runnerPlugin.beforeMethod());
    }

    public void runAfterTest(){
        runnerPlugins.forEach((aClass, runnerPlugin) -> runnerPlugin.afterMethod());
    }
}

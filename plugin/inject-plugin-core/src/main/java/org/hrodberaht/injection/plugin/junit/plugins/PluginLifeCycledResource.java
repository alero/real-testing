package org.hrodberaht.injection.plugin.junit.plugins;

import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.PluginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluginLifeCycledResource<T> {


    private static final Logger LOG = LoggerFactory.getLogger(PluginLifeCycledResource.class);

    private static InheritableThreadLocal<ThreadSafeState> threadState = new InheritableThreadLocal<>();

    public interface InstanceCreator<T> {
        T create();
    }

    public T create(Plugin.ResourceLifeCycle lifeCycle, PluginContext pluginContext, InstanceCreator<T> instanceCreator) {

        ThreadSafeState<T> threadSafeState = threadState.get();
        if (threadSafeState == null) {
            threadSafeState = new ThreadSafeState<>();
            threadState.set(threadSafeState);
        }

        if (lifeCycle == Plugin.ResourceLifeCycle.TEST_SUITE) {
            if (threadSafeState.suiteRunner == null) {
                threadSafeState.suiteRunner = createAndLogInstance(lifeCycle, instanceCreator);
            }
            return threadSafeState.suiteRunner;
        } else if (lifeCycle == Plugin.ResourceLifeCycle.TEST_CONFIG) {
            return threadSafeState.configClassRunner.computeIfAbsent(pluginContext.getConfigClass(), aClass -> createAndLogInstance(lifeCycle, instanceCreator));
        } else if (lifeCycle == Plugin.ResourceLifeCycle.TEST_CLASS) {
            return threadSafeState.testClassRunner.computeIfAbsent(pluginContext.getTestClass(), aClass -> createAndLogInstance(lifeCycle, instanceCreator));
        }

        return createAndLogInstance(lifeCycle, instanceCreator);
    }

    private T createAndLogInstance(Plugin.ResourceLifeCycle lifeCycle, InstanceCreator<T> instanceCreator) {
        T instance = instanceCreator.create();
        LOG.info("Created new resource {} using lifeCycle:{}", instance.getClass().getName(), lifeCycle);
        return instance;
    }

    private static class ThreadSafeState<T> {
        private T suiteRunner;
        private Map<Class, T> testClassRunner = new ConcurrentHashMap<>();
        private Map<Class, T> configClassRunner = new ConcurrentHashMap<>();
    }

}

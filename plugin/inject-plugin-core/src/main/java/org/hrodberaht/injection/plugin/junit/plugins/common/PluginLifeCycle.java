package org.hrodberaht.injection.plugin.junit.plugins.common;

import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.PluginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluginLifeCycle  {


    private static final Logger LOG = LoggerFactory.getLogger(PluginLifeCycle.class);
    private static Map<Class, StateHolder> classCache = new ConcurrentHashMap<>();


    private final Class<? extends Plugin> instanceClass;
    private final StateHolder stateHolder;

    public <T extends Plugin> PluginLifeCycle(Class<T> instanceClass) {
        this.instanceClass = instanceClass;
        stateHolder = classCache.computeIfAbsent(instanceClass, aClass -> new StateHolder());
    }

    private static class StateHolder<T> {
        private InheritableThreadLocal<ThreadSafeState<T>> threadState = new InheritableThreadLocal<>();
    }

    public interface InstanceCreator<T> {
        T create();
    }

    public <T extends Plugin> T create(Plugin.LifeCycle lifeCycle, Class configClass, InstanceCreator<T> instanceCreator) {

        ThreadSafeState<T> threadSafeState = getThreadSafeState();
        if (threadSafeState == null) {
            threadSafeState = new ThreadSafeState<>();
            stateHolder.threadState.set(threadSafeState);
        }

        if (lifeCycle == Plugin.LifeCycle.TEST_SUITE) {
            if (threadSafeState.suiteRunner == null) {
                threadSafeState.suiteRunner = createAndLogInstance(lifeCycle, instanceCreator);
            }
            return threadSafeState.suiteRunner;
        } else if (lifeCycle == Plugin.LifeCycle.TEST_CONFIG) {
            return threadSafeState.configClassRunner.computeIfAbsent(configClass, aClass -> createAndLogInstance(lifeCycle, instanceCreator));
        }

        return createAndLogInstance(lifeCycle, instanceCreator);
    }

    private <T extends Plugin> ThreadSafeState<T> getThreadSafeState() {

        return (ThreadSafeState<T>) stateHolder.threadState.get();
    }

    private <T extends Plugin> T createAndLogInstance(Plugin.LifeCycle lifeCycle, InstanceCreator<T> instanceCreator) {
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

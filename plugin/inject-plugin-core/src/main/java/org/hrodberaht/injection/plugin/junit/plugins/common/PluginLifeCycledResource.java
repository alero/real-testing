package org.hrodberaht.injection.plugin.junit.plugins.common;

import org.hrodberaht.injection.plugin.exception.PluginRuntimeException;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.PluginContext;
import org.hrodberaht.injection.plugin.junit.api.resource.FileLifeCycledResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluginLifeCycledResource<T> implements FileLifeCycledResource {


    private static final Logger LOG = LoggerFactory.getLogger(PluginLifeCycledResource.class);
    private static Map<Class, StateHolder> classCache = new ConcurrentHashMap<>();


    private final Class<T> instanceClass;
    private final StateHolder stateHolder;

    public PluginLifeCycledResource(Class<T> instanceClass) {
        this.instanceClass = instanceClass;
        stateHolder = classCache.computeIfAbsent(instanceClass, aClass -> new StateHolder());
    }

    public T create(Plugin.LifeCycle lifeCycle, PluginContext pluginContext, InstanceCreator<T> instanceCreator) {

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
            return threadSafeState.configClassRunner.computeIfAbsent(pluginContext.getConfigClass(), aClass -> createAndLogInstance(lifeCycle, instanceCreator));
        } else if (lifeCycle == Plugin.LifeCycle.TEST_CLASS) {
            return threadSafeState.testClassRunner.computeIfAbsent(pluginContext.getTestClass(), aClass -> createAndLogInstance(lifeCycle, instanceCreator));
        }

        return createAndLogInstance(lifeCycle, instanceCreator);
    }

    private ThreadSafeState<T> getThreadSafeState() {

        return (ThreadSafeState<T>) stateHolder.threadState.get();
    }

    private T createAndLogInstance(Plugin.LifeCycle lifeCycle, InstanceCreator<T> instanceCreator) {
        T instance = instanceCreator.create();
        LOG.info("Created new resource {} using lifeCycle:{}", instance.getClass().getName(), lifeCycle);
        return instance;
    }

    /**
     * Append separators to a file directory that is unique for each test resource depending on lifecycle selection (cares about multi-threading)
     *
     * @param base          the base of the directory,
     * @param pluginContext the plugincontext to use to make unique paths
     * @param lifeCycle     the selected lifecycle of the resource to store
     * @return a new threadsafe and test-lifecycle unique directory
     */
    public String testDirectory(String base, PluginContext pluginContext, Plugin.LifeCycle lifeCycle) {
        String threadName = Thread.currentThread().getName();
        if (lifeCycle == Plugin.LifeCycle.TEST) {
            return base + File.separator + threadName + File.separator + pluginContext.getTestClass().getSimpleName() + File.separator + pluginContext.getTestName();
        } else if (lifeCycle == Plugin.LifeCycle.TEST_CONFIG) {
            return base + File.separator + threadName + File.separator + pluginContext.getConfigClass().getSimpleName();
        } else if (lifeCycle == Plugin.LifeCycle.TEST_CLASS) {
            return base + File.separator + threadName + File.separator + pluginContext.getTestClass().getSimpleName();
        } else if (lifeCycle == Plugin.LifeCycle.TEST_SUITE) {
            return base + File.separator + threadName + File.separator + "suite";
        }
        throw new PluginRuntimeException("No home was selected");
    }

    public interface InstanceCreator<T> {
        T create();
    }

    private static class StateHolder<T> {
        private InheritableThreadLocal<ThreadSafeState<T>> threadState = new InheritableThreadLocal<>();
    }

    private static class ThreadSafeState<T> {
        private T suiteRunner;
        private Map<Class, T> testClassRunner = new ConcurrentHashMap<>();
        private Map<Class, T> configClassRunner = new ConcurrentHashMap<>();
    }

}

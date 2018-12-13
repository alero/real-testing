/*
 * Copyright (c) 2017 org.hrodberaht
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.PluginContext;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterClassTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterContainerCreation;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeClassTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeContainerCreation;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RunnerPlugins {

    private final Map<Class<? extends Plugin>, Plugin> activePlugins = new ConcurrentHashMap<>();
    private final List<Plugin> sortedRunnerPlugins = new ArrayList<>();
    private final TestRunnerPlugins testRunnerPlugins;
    private final TestClassRunnerPlugins testClassRunnerPlugins;
    private final TestConfigRunnerPlugins testConfigRunnerPlugins;
    private final TestSuiteRunnerPlugins testSuiteRunnerPlugins;

    public RunnerPlugins() {
        testRunnerPlugins = new TestRunnerPlugins(activePlugins);
        testClassRunnerPlugins = new TestClassRunnerPlugins(activePlugins);
        testConfigRunnerPlugins = new TestConfigRunnerPlugins(activePlugins);
        testSuiteRunnerPlugins = new TestSuiteRunnerPlugins(activePlugins);
    }


    public <T extends Plugin> T addAnnotatedPlugin(Plugin runnerPlugin) {
        sortedRunnerPlugins.add(runnerPlugin);
        return (T) getRunner(runnerPlugin.getLifeCycle()).addAnnotatedPlugin(runnerPlugin);
    }

    private RunnerPluginInterface getRunner(Plugin.LifeCycle lifeCycle) {
        switch (lifeCycle) {
            case TEST_CLASS:
                return testClassRunnerPlugins;
            case TEST_CONFIG:
                return testConfigRunnerPlugins;
            case TEST_SUITE:
                return testSuiteRunnerPlugins;
            case TEST:
                return testRunnerPlugins;
            default:
                throw new RuntimeException("Not supported plugin lifecycle selected");
        }
    }

    private void runInitAfterContainerAnnotation(PluginContext pluginContext) {
        for (Plugin plugin : sortedRunnerPlugins) {
            findAnnotationAndInvokeMethod(plugin.getClass(), pluginContext, getRunner(plugin.getLifeCycle()), RunnerPluginAfterContainerCreation.class);
        }
    }

    private void runInitBeforeContainerAnnotation(PluginContext pluginContext) {
        for (Plugin plugin : sortedRunnerPlugins) {
            findAnnotationAndInvokeMethod(plugin.getClass(), pluginContext, getRunner(plugin.getLifeCycle()), RunnerPluginBeforeContainerCreation.class);
        }
    }

    private void runAfterTestClassAnnotation(PluginContext pluginContext) {
        for (Plugin plugin : sortedRunnerPlugins) {
            findAnnotationAndInvokeMethod(plugin.getClass(), pluginContext, getRunner(plugin.getLifeCycle()), RunnerPluginAfterClassTest.class);
        }
    }

    private void runBeforeTestClassAnnotation(PluginContext pluginContext) {
        for (Plugin plugin : sortedRunnerPlugins) {
            findAnnotationAndInvokeMethod(plugin.getClass(), pluginContext, getRunner(plugin.getLifeCycle()), RunnerPluginBeforeClassTest.class);
        }
    }

    private void runBeforeTestAnnotation(PluginContext pluginContext) {
        for (Plugin plugin : sortedRunnerPlugins) {
            findAnnotationAndInvokeMethod(plugin.getClass(), pluginContext, getRunner(plugin.getLifeCycle()), RunnerPluginBeforeTest.class);
        }
    }

    private void runAfterTestAnnotation(PluginContext pluginContext) {
        for (Plugin plugin : sortedRunnerPlugins) {
            findAnnotationAndInvokeMethod(plugin.getClass(), pluginContext, getRunner(plugin.getLifeCycle()), RunnerPluginAfterTest.class);
        }
    }


    private void findAnnotationAndInvokeMethod(Class pluginClass, PluginContext pluginContext, RunnerPluginInterface runner, Class annotation) {
        runner.findAnnotationAndInvokeMethod(pluginClass, pluginContext, annotation);
    }

    public void runInitBeforeContainer(PluginContext pluginContext) {
        runInitBeforeContainerAnnotation(pluginContext);
    }

    public void runInitAfterContainer(PluginContext pluginContext) {
        runInitAfterContainerAnnotation(pluginContext);
    }

    public void runBeforeTest(PluginContext pluginContext) {
        runBeforeTestAnnotation(pluginContext);
    }

    public void runAfterTest(PluginContext pluginContext) {
        runAfterTestAnnotation(pluginContext);
    }

    public void runBeforeTestClass(PluginContext pluginContext) {
        runBeforeTestClassAnnotation(pluginContext);
    }

    public void runAfterTestClass(PluginContext pluginContext) {
        runAfterTestClassAnnotation(pluginContext);
    }

    public <T extends Plugin> void addActivePlugin(Class<T> pluginClass, T plugin) {
        activePlugins.put(pluginClass, plugin);
    }
}

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
import org.hrodberaht.injection.plugin.junit.api.RunnerPlugin;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterClassTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterContainerCreation;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeClassTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeContainerCreation;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeTest;

import java.util.Map;

public class RunnerPlugins {

    private final TestConfigRunnerPlugins testConfigRunnerPlugins;
    private final TestClassRunnerPlugins testClassRunnerPlugins;
    private final TestSuiteRunnerPlugins testSuiteRunnerPlugins;

    public RunnerPlugins(Map<Class<? extends Plugin>, Plugin> activePlugins) {
        testClassRunnerPlugins = new TestClassRunnerPlugins(activePlugins);
        testConfigRunnerPlugins = new TestConfigRunnerPlugins(activePlugins);
        testSuiteRunnerPlugins = new TestSuiteRunnerPlugins(activePlugins);
    }

    public <T extends Plugin> T addPlugin(RunnerPlugin runnerPlugin) {
        return (T) getRunner(runnerPlugin.getLifeCycle()).addPlugin(runnerPlugin);
    }

    public <T extends Plugin> T addAnnotatedPlugin(Plugin runnerPlugin) {
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
            default:
                throw new RuntimeException("Not supported plugin lifecycle selected");
        }
    }

    private void runInitAfterContainerAnnotation(PluginContext pluginContext) {
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_SUITE), RunnerPluginAfterContainerCreation.class);
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_CONFIG), RunnerPluginAfterContainerCreation.class);
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_CLASS), RunnerPluginAfterContainerCreation.class);
    }

    private void runInitBeforerContainerAnnotation(PluginContext pluginContext) {
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_SUITE), RunnerPluginBeforeContainerCreation.class);
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_CONFIG), RunnerPluginBeforeContainerCreation.class);
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_CLASS), RunnerPluginBeforeContainerCreation.class);
    }

    private void runBeforeTestAnnotation(PluginContext pluginContext) {
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_SUITE), RunnerPluginBeforeTest.class);
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_CONFIG), RunnerPluginBeforeTest.class);
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_CLASS), RunnerPluginBeforeTest.class);
    }

    private void runAfterTestAnnotation(PluginContext pluginContext) {
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_SUITE), RunnerPluginAfterTest.class);
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_CONFIG), RunnerPluginAfterTest.class);
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_CLASS), RunnerPluginAfterTest.class);
    }

    private void runAfterTestClassAnnotation(PluginContext pluginContext) {
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_SUITE), RunnerPluginAfterClassTest.class);
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_CONFIG), RunnerPluginAfterClassTest.class);
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_CLASS), RunnerPluginAfterClassTest.class);
    }

    private void runBeforeTestClassAnnotation(PluginContext pluginContext) {
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_SUITE), RunnerPluginBeforeClassTest.class);
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_CONFIG), RunnerPluginBeforeClassTest.class);
        findAnnotationAndInvokeMethod(pluginContext, getRunner(Plugin.LifeCycle.TEST_CLASS), RunnerPluginBeforeClassTest.class);
    }

    private void findAnnotationAndInvokeMethod(PluginContext pluginContext, RunnerPluginInterface runner, Class annotation) {
        runner.findAnnotationAndInvokeMethod(pluginContext, annotation);
    }

    public void runInitBeforeContainer(PluginContext pluginContext) {
        runInitBeforerContainerAnnotation(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_CONFIG).runInitBeforeContainer(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_CLASS).runInitBeforeContainer(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_SUITE).runInitBeforeContainer(pluginContext);
    }

    public void runInitAfterContainer(PluginContext pluginContext) {
        runInitAfterContainerAnnotation(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_CONFIG).runInitAfterContainer(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_CLASS).runInitAfterContainer(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_SUITE).runInitAfterContainer(pluginContext);
    }

    public void runBeforeTest(PluginContext pluginContext) {
        runBeforeTestAnnotation(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_CONFIG).runBeforeTest(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_CLASS).runBeforeTest(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_SUITE).runBeforeTest(pluginContext);
    }

    public void runAfterTest(PluginContext pluginContext) {
        runAfterTestAnnotation(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_CONFIG).runAfterTest(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_CLASS).runAfterTest(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_SUITE).runAfterTest(pluginContext);
    }

    public void runBeforeTestClass(PluginContext pluginContext) {
        runBeforeTestClassAnnotation(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_CONFIG).runBeforeTestClass(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_CLASS).runBeforeTestClass(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_SUITE).runBeforeTestClass(pluginContext);
    }

    public void runAfterTestClass(PluginContext pluginContext) {
        runAfterTestClassAnnotation(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_CONFIG).runAfterTestClass(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_CLASS).runAfterTestClass(pluginContext);
        getRunner(Plugin.LifeCycle.TEST_SUITE).runAfterTestClass(pluginContext);
    }
}

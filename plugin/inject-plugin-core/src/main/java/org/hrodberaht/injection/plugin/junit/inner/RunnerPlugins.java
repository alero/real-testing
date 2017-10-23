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

import org.hrodberaht.injection.plugin.junit.spi.Plugin;
import org.hrodberaht.injection.plugin.junit.spi.RunnerPlugin;
import org.hrodberaht.injection.plugin.junit.spi.annotation.RunnerPluginAfterClassTest;
import org.hrodberaht.injection.plugin.junit.spi.annotation.RunnerPluginAfterContainerCreation;
import org.hrodberaht.injection.plugin.junit.spi.annotation.RunnerPluginAfterTest;
import org.hrodberaht.injection.plugin.junit.spi.annotation.RunnerPluginBeforeClassTest;
import org.hrodberaht.injection.plugin.junit.spi.annotation.RunnerPluginBeforeContainerCreation;
import org.hrodberaht.injection.plugin.junit.spi.annotation.RunnerPluginBeforeTest;
import org.hrodberaht.injection.core.register.InjectionRegister;

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
            case TEST_CONFIG:
                return testConfigRunnerPlugins;
            case TEST_CLASS:
                return testClassRunnerPlugins;
            case TEST_SUITE:
                return testSuiteRunnerPlugins;
            default:
                throw new RuntimeException("Not supported plugin lifecycle selected");
        }
    }

    private void runInitAfterContainerAnnotation(InjectionRegister injectionRegister) {
        findAnnotationAndInvokeMethod(injectionRegister, getRunner(Plugin.LifeCycle.TEST_SUITE), RunnerPluginAfterContainerCreation.class);
        findAnnotationAndInvokeMethod(injectionRegister, getRunner(Plugin.LifeCycle.TEST_CONFIG), RunnerPluginAfterContainerCreation.class);
        findAnnotationAndInvokeMethod(injectionRegister, getRunner(Plugin.LifeCycle.TEST_CLASS), RunnerPluginAfterContainerCreation.class);
    }

    private void runInitBeforerContainerAnnotation() {
        findAnnotationAndInvokeMethod(null, getRunner(Plugin.LifeCycle.TEST_SUITE), RunnerPluginBeforeContainerCreation.class);
        findAnnotationAndInvokeMethod(null, getRunner(Plugin.LifeCycle.TEST_CONFIG), RunnerPluginBeforeContainerCreation.class);
        findAnnotationAndInvokeMethod(null, getRunner(Plugin.LifeCycle.TEST_CLASS), RunnerPluginBeforeContainerCreation.class);
    }

    private void runBeforeTestAnnotation(InjectionRegister injectionRegister) {
        findAnnotationAndInvokeMethod(injectionRegister, getRunner(Plugin.LifeCycle.TEST_SUITE), RunnerPluginBeforeTest.class);
        findAnnotationAndInvokeMethod(injectionRegister, getRunner(Plugin.LifeCycle.TEST_CONFIG), RunnerPluginBeforeTest.class);
        findAnnotationAndInvokeMethod(injectionRegister, getRunner(Plugin.LifeCycle.TEST_CLASS), RunnerPluginBeforeTest.class);
    }

    private void runAfterTestAnnotation(InjectionRegister injectionRegister) {
        findAnnotationAndInvokeMethod(injectionRegister, getRunner(Plugin.LifeCycle.TEST_SUITE), RunnerPluginAfterTest.class);
        findAnnotationAndInvokeMethod(injectionRegister, getRunner(Plugin.LifeCycle.TEST_CONFIG), RunnerPluginAfterTest.class);
        findAnnotationAndInvokeMethod(injectionRegister, getRunner(Plugin.LifeCycle.TEST_CLASS), RunnerPluginAfterTest.class);
    }

    private void runAfterTestClassAnnotation(InjectionRegister injectionRegister) {
        findAnnotationAndInvokeMethod(injectionRegister, getRunner(Plugin.LifeCycle.TEST_SUITE), RunnerPluginAfterClassTest.class);
        findAnnotationAndInvokeMethod(injectionRegister, getRunner(Plugin.LifeCycle.TEST_CONFIG), RunnerPluginAfterClassTest.class);
        findAnnotationAndInvokeMethod(injectionRegister, getRunner(Plugin.LifeCycle.TEST_CLASS), RunnerPluginAfterClassTest.class);
    }

    private void runBeforeTestClassAnnotation(InjectionRegister injectionRegister) {
        findAnnotationAndInvokeMethod(injectionRegister, getRunner(Plugin.LifeCycle.TEST_SUITE), RunnerPluginBeforeClassTest.class);
        findAnnotationAndInvokeMethod(injectionRegister, getRunner(Plugin.LifeCycle.TEST_CONFIG), RunnerPluginBeforeClassTest.class);
        findAnnotationAndInvokeMethod(injectionRegister, getRunner(Plugin.LifeCycle.TEST_CLASS), RunnerPluginBeforeClassTest.class);
    }

    private void findAnnotationAndInvokeMethod(InjectionRegister injectionRegister, RunnerPluginInterface runner, Class annotation) {
        runner.findAnnotationAndInvokeMethod(injectionRegister, annotation);
    }

    public void runInitBeforeContainer() {
        runInitBeforerContainerAnnotation();
        getRunner(Plugin.LifeCycle.TEST_CONFIG).runInitBeforeContainer();
        getRunner(Plugin.LifeCycle.TEST_CLASS).runInitBeforeContainer();
        getRunner(Plugin.LifeCycle.TEST_SUITE).runInitBeforeContainer();
    }

    public void runInitAfterContainer(InjectionRegister injectionRegister) {
        runInitAfterContainerAnnotation(injectionRegister);
        getRunner(Plugin.LifeCycle.TEST_CONFIG).runInitAfterContainer(injectionRegister);
        getRunner(Plugin.LifeCycle.TEST_CLASS).runInitAfterContainer(injectionRegister);
        getRunner(Plugin.LifeCycle.TEST_SUITE).runInitAfterContainer(injectionRegister);
    }

    public void runBeforeTest(InjectionRegister injectionRegister) {
        runBeforeTestAnnotation(injectionRegister);
        getRunner(Plugin.LifeCycle.TEST_CONFIG).runBeforeTest(injectionRegister);
        getRunner(Plugin.LifeCycle.TEST_CLASS).runBeforeTest(injectionRegister);
        getRunner(Plugin.LifeCycle.TEST_SUITE).runBeforeTest(injectionRegister);
    }

    public void runAfterTest(InjectionRegister injectionRegister) {
        runAfterTestAnnotation(injectionRegister);
        getRunner(Plugin.LifeCycle.TEST_CONFIG).runAfterTest(injectionRegister);
        getRunner(Plugin.LifeCycle.TEST_CLASS).runAfterTest(injectionRegister);
        getRunner(Plugin.LifeCycle.TEST_SUITE).runAfterTest(injectionRegister);
    }

    public void runBeforeTestClass(InjectionRegister injectionRegister) {
        runBeforeTestClassAnnotation(injectionRegister);
        getRunner(Plugin.LifeCycle.TEST_CONFIG).runBeforeTestClass(injectionRegister);
        getRunner(Plugin.LifeCycle.TEST_CLASS).runBeforeTestClass(injectionRegister);
        getRunner(Plugin.LifeCycle.TEST_SUITE).runBeforeTestClass(injectionRegister);
    }

    public void runAfterTestClass(InjectionRegister injectionRegister) {
        runAfterTestClassAnnotation(injectionRegister);
        getRunner(Plugin.LifeCycle.TEST_CONFIG).runAfterTestClass(injectionRegister);
        getRunner(Plugin.LifeCycle.TEST_CLASS).runAfterTestClass(injectionRegister);
        getRunner(Plugin.LifeCycle.TEST_SUITE).runAfterTestClass(injectionRegister);
    }
}

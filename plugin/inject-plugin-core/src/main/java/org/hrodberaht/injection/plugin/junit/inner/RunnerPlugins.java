package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.plugin.junit.spi.Plugin;
import org.hrodberaht.injection.plugin.junit.spi.RunnerPlugin;
import org.hrodberaht.injection.register.InjectionRegister;

public class RunnerPlugins {

    private TestRunnerPlugins testRunnerPlugins = new TestRunnerPlugins();
    private TestClassRunnerPlugins testClassRunnerPlugins = new TestClassRunnerPlugins();
    private TestSuiteRunnerPlugins testSuiteRunnerPlugins = new TestSuiteRunnerPlugins();

    public <T extends Plugin> T addPlugin(RunnerPlugin runnerPlugin) {
        return (T) getRunner(runnerPlugin.getLifeCycle()).addPlugin(runnerPlugin);
    }

    private RunnerPluginInterface getRunner(Plugin.LifeCycle lifeCycle){
        switch (lifeCycle){
            case TEST: return testRunnerPlugins;
            case TESTCLASS: return testClassRunnerPlugins;
            case TESTSUITE: return testSuiteRunnerPlugins;
            default: throw new RuntimeException("Not supported plugin lifecycle selected");
        }
    }

    public void runInitBeforeContainer() {
        getRunner(Plugin.LifeCycle.TEST).runInitBeforeContainer();
        getRunner(Plugin.LifeCycle.TESTCLASS).runInitBeforeContainer();
        getRunner(Plugin.LifeCycle.TESTSUITE).runInitBeforeContainer();
    }

    public void runInitAfterContainer(InjectionRegister injectionRegister) {
        getRunner(Plugin.LifeCycle.TEST).runInitAfterContainer(injectionRegister);
        getRunner(Plugin.LifeCycle.TESTCLASS).runInitAfterContainer(injectionRegister);
        getRunner(Plugin.LifeCycle.TESTSUITE).runInitAfterContainer(injectionRegister);
    }

    public void runBeforeTest(InjectionRegister injectionRegister) {
        getRunner(Plugin.LifeCycle.TEST).runBeforeTest(injectionRegister);
        getRunner(Plugin.LifeCycle.TESTCLASS).runBeforeTest(injectionRegister);
        getRunner(Plugin.LifeCycle.TESTSUITE).runBeforeTest(injectionRegister);
    }

    public void runAfterTest(InjectionRegister injectionRegister) {
        getRunner(Plugin.LifeCycle.TEST).runAfterTest(injectionRegister);
        getRunner(Plugin.LifeCycle.TESTCLASS).runAfterTest(injectionRegister);
        getRunner(Plugin.LifeCycle.TESTSUITE).runAfterTest(injectionRegister);
    }

    public void runBeforeTestClass(InjectionRegister injectionRegister) {
        getRunner(Plugin.LifeCycle.TEST).runBeforeTestClass(injectionRegister);
        getRunner(Plugin.LifeCycle.TESTCLASS).runBeforeTestClass(injectionRegister);
        getRunner(Plugin.LifeCycle.TESTSUITE).runBeforeTestClass(injectionRegister);
    }

    public void runAfterTestClass(InjectionRegister injectionRegister) {
        getRunner(Plugin.LifeCycle.TEST).runAfterTestClass(injectionRegister);
        getRunner(Plugin.LifeCycle.TESTCLASS).runAfterTestClass(injectionRegister);
        getRunner(Plugin.LifeCycle.TESTSUITE).runAfterTestClass(injectionRegister);
    }
}

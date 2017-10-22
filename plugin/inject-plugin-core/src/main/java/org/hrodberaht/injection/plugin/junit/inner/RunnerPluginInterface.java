package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.plugin.junit.spi.RunnerPlugin;
import org.hrodberaht.injection.register.InjectionRegister;

public interface RunnerPluginInterface {
    RunnerPlugin addPlugin(RunnerPlugin runnerPlugin);
    void runInitBeforeContainer();

    void runAfterTestClass(InjectionRegister injectionRegister);

    void runBeforeTestClass(InjectionRegister injectionRegister);

    void runAfterTest(InjectionRegister injectionRegister);

    void runBeforeTest(InjectionRegister injectionRegister);

    void runInitAfterContainer(InjectionRegister injectionRegister);
}

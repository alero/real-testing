package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.plugin.junit.spi.Plugin;
import org.hrodberaht.injection.plugin.junit.spi.RunnerPlugin;
import org.hrodberaht.injection.register.InjectionRegister;

import java.lang.annotation.Annotation;

public interface RunnerPluginInterface {
    RunnerPlugin addPlugin(RunnerPlugin runnerPlugin);
    Plugin addAnnotatedPlugin(Plugin runnerPlugin);
    void runInitBeforeContainer();

    void runAfterTestClass(InjectionRegister injectionRegister);

    void runBeforeTestClass(InjectionRegister injectionRegister);

    void runAfterTest(InjectionRegister injectionRegister);

    void runBeforeTest(InjectionRegister injectionRegister);

    void runInitAfterContainer(InjectionRegister injectionRegister);

    void findAnnotationAndInvokeMethod(InjectionRegister injectionRegister, Class<Annotation> annotation);
}

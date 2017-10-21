package org.hrodberaht.injection.plugin.junit.spi;

import org.hrodberaht.injection.register.InjectionRegister;

public interface RunnerPlugin extends Plugin {

    void beforeContainerCreation();

    void afterContainerCreation(InjectionRegister injectionRegister);

    void beforeTest(InjectionRegister injectionRegister);

    void beforeTestClass(InjectionRegister injectionRegister);

    void afterTestClass(InjectionRegister injectionRegister);

    void afterTest(InjectionRegister injectionRegister);

}

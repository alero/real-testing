package org.hrodberaht.injection.plugin.junit.spi;

import org.hrodberaht.injection.register.InjectionRegister;

public interface RunnerPlugin extends Plugin {

    void beforeContainerCreation();

    void afterContainerCreation(InjectionRegister injectionRegister);

    void beforeMethod(InjectionRegister injectionRegister);

    void afterMethod(InjectionRegister injectionRegister);

}

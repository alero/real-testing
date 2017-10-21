package org.hrodberaht.injection.plugin.junit.cdi;

import org.hrodberaht.injection.register.InjectionRegister;

/**
 * Created by alexbrob on 2016-03-31.
 */
public interface CDIExtensions {
    void runAfterBeanDiscovery(InjectionRegister register);

    void runBeforeBeanDiscovery();
}

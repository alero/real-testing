package org.hrodberaht.injection.spi;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.register.InjectionRegister;


/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-11-28
 * Time: 11:03
 * To change this template use File | Settings | File Templates.
 */
public interface ContainerConfigBuilder {
    void cleanActiveContainer();

    InjectionRegister getActiveRegister();

    void addSingletonActiveRegistry();

    void start();

    void injectResources(Object service);
}

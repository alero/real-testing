package org.hrodberaht.inject.spi;

import org.hrodberaht.inject.InjectContainer;


/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-11-28
 * Time: 11:03
 * To change this template use File | Settings | File Templates.
 */
public interface ContainerConfig {
    void cleanActiveContainer();

    InjectionRegisterScanInterface getActiveRegister();

    InjectContainer getActiveContainer();

    ResourceCreator getResourceCreator();

    InjectContainer createContainer();


}

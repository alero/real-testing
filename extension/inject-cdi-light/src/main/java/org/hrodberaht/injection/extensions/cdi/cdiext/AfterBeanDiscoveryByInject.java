package org.hrodberaht.injection.extensions.cdi.cdiext;

import org.hrodberaht.injection.InjectionRegisterModule;

import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.ObserverMethod;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-12-03
 * Time: 14:06
 * To change this template use File | Settings | File Templates.
 */
public class AfterBeanDiscoveryByInject implements AfterBeanDiscovery {

    private InjectionRegisterModule injectionRegisterScanInterface;

    public AfterBeanDiscoveryByInject(InjectionRegisterModule injectionRegisterScanInterface) {
        this.injectionRegisterScanInterface = injectionRegisterScanInterface;
    }

    public void addDefinitionError(Throwable throwable) {

    }

    public void addBean(Bean<?> bean) {

    }

    public void addObserverMethod(ObserverMethod<?> observerMethod) {

    }

    public void addContext(Context context) {

    }
}

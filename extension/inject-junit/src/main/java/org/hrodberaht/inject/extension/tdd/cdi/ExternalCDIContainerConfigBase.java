package org.hrodberaht.inject.extension.tdd.cdi;

import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.extension.cdi.inner.InjectionRegisterScanCDI;
import org.hrodberaht.inject.extension.tdd.internal.ProxyResourceCreator;
import org.hrodberaht.inject.spi.ResourceCreator;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-11-28
 * Time: 10:37
 * To change this template use File | Settings | File Templates.
 */
public abstract class ExternalCDIContainerConfigBase extends TDDCDIContainerConfigBase {

    protected ExternalCDIContainerConfigBase(ResourceCreator resourceCreator) {
        super(resourceCreator);
    }

    protected ExternalCDIContainerConfigBase() {
        resourceCreator = new ProxyResourceCreator();
    }


    @Override
    /**
     * Return an empty container that just wraps the "extended resource controller"
     */
    public InjectContainer createContainer() {
        this.originalRegister = new InjectionRegisterScanCDI();
        this.activeRegister = originalRegister;
        appendTypedResources();
        return activeRegister.getInjectContainer();
    }
}

package test.org.hrodberaht.inject.annotation.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.config.ContainerConfigBase;
import org.hrodberaht.injection.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.spi.ResourceCreator;

/**
 * Created by alexbrob on 2016-03-29.
 */
public class TestConfig extends ContainerConfigBase {



    @Override
    protected ResourceCreator createResourceCreator() {
        return null;
    }

    @Override
    public ResourceCreator getResourceCreator() {
        return null;
    }

    @Override
    public InjectContainer createContainer() {
        return null;
    }

    @Override
    protected void injectResources(Object serviceInstance) {

    }

    @Override
    protected InjectionRegisterScanBase getScanner(InjectionRegister registerModule) {
        return null;
    }

}

package org.hrodberaht.injection.extensions.cdi;

import org.hrodberaht.injection.spi.ResourceCreator;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-12-04
 * Time: 07:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class CDIContainerConfig extends CDIContainerConfigBase {

    protected CDIContainerConfig(ResourceCreator resourceCreator) {
        super(resourceCreator);
    }

    protected CDIContainerConfig() {
        super();
    }



}

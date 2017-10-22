package org.hrodberaht.injection.plugin.junit.resources;

import org.hrodberaht.injection.spi.JavaResourceCreator;
import org.hrodberaht.injection.spi.ResourceFactory;

public abstract class ResourcePluginBase {

    ResourceFactory resourceFactory;

    public <T> JavaResourceCreator<T> getCreator(Class<T> aClass) {
        return resourceFactory.getCreator(aClass);
    }

}

package org.hrodberaht.injection.plugin.junit.spi;

import org.hrodberaht.injection.plugin.junit.resources.PluggableResourceFactory;

public interface Plugin {

    LifeCycle getLifeCycle();

    enum LifeCycle { SINGLETON, NEW }

}

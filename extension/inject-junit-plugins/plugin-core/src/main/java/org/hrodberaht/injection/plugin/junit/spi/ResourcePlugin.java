package org.hrodberaht.injection.plugin.junit.spi;

import org.hrodberaht.injection.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.plugin.junit.resources.ChainableInjectionPointProvider;
import org.hrodberaht.injection.plugin.junit.resources.PluggableResourceFactory;
import org.hrodberaht.injection.spi.JavaResourceCreator;

import java.util.List;

public interface ResourcePlugin {
    List<Class> getCustomTypes();

    <T> JavaResourceCreator<T> getCreator(Class<T> aClass);

    <T> JavaResourceCreator<T> getInnerCreator(Class<T> aClass);

    void setPluggableResourceFactory(PluggableResourceFactory pluggableResourceFactory);

    default ChainableInjectionPointProvider getInjectionProvider(InjectionFinder injectionFinder) {
        return null;
    }
}

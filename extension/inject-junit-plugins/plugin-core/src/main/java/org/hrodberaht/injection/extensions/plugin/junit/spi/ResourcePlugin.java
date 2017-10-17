package org.hrodberaht.injection.extensions.plugin.junit.spi;

import org.hrodberaht.injection.spi.JavaResourceCreator;

import java.util.List;

public interface ResourcePlugin {
    List<Class> getCustomTypes();

    <T> JavaResourceCreator<T> getCreator(Class<T> aClass);
}

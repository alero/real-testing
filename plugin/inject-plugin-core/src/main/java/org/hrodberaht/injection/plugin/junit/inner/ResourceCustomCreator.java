package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.spi.JavaResourceCreator;

import java.util.List;

public interface ResourceCustomCreator {
    List<Class> getCustomTypes();

    <T> JavaResourceCreator<T> getCreator(Class<T> aClass);
}

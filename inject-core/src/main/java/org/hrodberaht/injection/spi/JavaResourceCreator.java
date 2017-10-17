package org.hrodberaht.injection.spi;

public interface JavaResourceCreator<T> {

    T create(String name);
    T create();

    T create(String name, T instance);
    T create(T instance);

}

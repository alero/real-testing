package org.hrodberaht.injection;

/**
 * Created by alexbrob on 2016-03-30.
 */
public interface InjectionRegistry<T extends Module> {
    InjectContainer getContainer();
    T getModule();
}

package org.hrodberaht.injection.stream;

/**
 * Created by alexbrob on 2016-03-30.
 */
@FunctionalInterface
public interface RegisterModuleFunc {
    void register(Registrations registrations);
}

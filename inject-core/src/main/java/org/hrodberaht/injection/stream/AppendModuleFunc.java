package org.hrodberaht.injection.stream;

import org.hrodberaht.injection.Module;

/**
 * Created by alexbrob on 2016-03-30.
 */
@FunctionalInterface
public interface AppendModuleFunc {
    Module module();
}

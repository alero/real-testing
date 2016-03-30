package org.hrodberaht.injection.config;

import org.hrodberaht.injection.internal.ScopeContainer;

/**
 * Created by alexbrob on 2016-03-01.
 */
public interface ScanningService {

    ScopeContainer.Scope getScope(Class aClazz);

    boolean isServiceAnnotated(Class aClazz);

    boolean isInterfaceAnnotated(Class aClazz);

    void registerForScanner(Class aClazz, Class serviceClass, ScopeContainer.Scope scope);
}

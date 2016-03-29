package org.hrodberaht.injection.register.internal;

import org.hrodberaht.injection.InjectionContainerManager;
import org.hrodberaht.injection.ScopeContainer;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jun-17 20:22:48
 * @version 1.0
 * @since 1.0
 */
public interface RegistrationExtended extends Registration {
    RegistrationExtended annotated(Class<? extends Annotation> annotation);

    RegistrationExtended named(String named);

    RegistrationExtended registerTypeAs(InjectionContainerManager.RegisterType registerType);

    RegistrationExtended scopeAs(ScopeContainer.Scope scope);


}
package org.hrodberaht.inject.register;

import org.hrodberaht.inject.InjectContainer;

import java.util.Collection;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-jun-05 21:13:55
 * @version 1.0
 * @since 1.0
 */
public interface RegistrationModule<T> {
    Collection<T> getRegistrations();

    void postRegistration(InjectContainer injectContainer);

    void preRegistration(InjectContainer injectContainer);
}

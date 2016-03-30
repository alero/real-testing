package org.hrodberaht.injection.register;

import org.hrodberaht.injection.InjectContainer;

import java.util.Collection;
import java.util.List;

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

    List<T> getRegistrationsList();

    void postRegistration(InjectContainer injectContainer);

    void preRegistration(InjectContainer injectContainer);
}

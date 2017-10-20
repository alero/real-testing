package org.hrodberaht.injection.register;

import org.hrodberaht.injection.register.internal.RegistrationInstanceSimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-jun-03 17:49:42
 * @version 1.0
 * @since 1.0
 */
public abstract class ExtendedModule extends RegistrationModuleAnnotation {
    protected RegistrationModuleAnnotation registration = null;

    public Collection<RegistrationInstanceSimple> getRegistrations() {
        return registration.getRegistrations();
    }

    @Override
    public List<RegistrationInstanceSimple> getRegistrationsList() {
        return new ArrayList<>(getRegistrations());
    }

    @Override
    public void registrations() {
        // just do nothing
    }

}

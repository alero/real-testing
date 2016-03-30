package org.hrodberaht.injection.register.internal;

import org.hrodberaht.injection.internal.InjectionContainerManager;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-jun-03 17:53:13
 * @version 1.0
 * @since 1.0
 */
public class RegistrationInstanceSimple extends RegistrationInstanceAnnotation<RegistrationInstanceSimple>
        implements RegistrationExtended {

    private InjectionContainerManager.RegisterType registerType = InjectionContainerManager.RegisterType.NORMAL;


    public RegistrationInstanceSimple(Class theInterface) {
        super(theInterface);
    }

    public RegistrationExtended registerTypeAs(InjectionContainerManager.RegisterType registerType) {
        this.registerType = registerType;
        return this;
    }


    public InjectionContainerManager.RegisterType getRegisterType() {
        return registerType;
    }

}

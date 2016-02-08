package org.hrodberaht.inject.register.internal;

import org.hrodberaht.inject.SimpleInjection;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-jun-03 17:53:13
 * @version 1.0
 * @since 1.0
 */
public class RegistrationInstanceSimple extends RegistrationInstanceAnnotation<RegistrationExtended>
        implements RegistrationExtended {

    private SimpleInjection.RegisterType registerType = SimpleInjection.RegisterType.NORMAL;


    public RegistrationInstanceSimple(Class theInterface) {
        super(theInterface);
    }

    public RegistrationExtended registerTypeAs(SimpleInjection.RegisterType registerType) {
        this.registerType = registerType;
        return this;
    }


    public SimpleInjection.RegisterType getRegisterType() {
        return registerType;
    }

}

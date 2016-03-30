package org.hrodberaht.injection.stream;

import org.hrodberaht.injection.register.internal.RegistrationExtended;
import org.hrodberaht.injection.register.internal.RegistrationInstanceSimple;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class Registrations {

    private List<RegistrationInstanceSimple> simpleCollection = new ArrayList<>();

    public RegistrationExtended create(Class serviceClass) {
        RegistrationInstanceSimple registrationInstanceSimple = new RegistrationInstanceSimple(serviceClass);
        simpleCollection.add(registrationInstanceSimple);
        return registrationInstanceSimple;
    }

    protected List<RegistrationInstanceSimple> registry() {
        return simpleCollection;
    }
}

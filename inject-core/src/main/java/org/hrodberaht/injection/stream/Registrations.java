package org.hrodberaht.injection.stream;

import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.register.internal.RegistrationExtended;
import org.hrodberaht.injection.register.internal.RegistrationInstanceSimple;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class Registrations {

    private List<RegistrationInstanceSimple> simpleCollection = new ArrayList<>();
    private List<RegistrationModuleAnnotation> modulesCollection = new ArrayList<>();

    public RegistrationExtended register(Class serviceClass) {
        RegistrationInstanceSimple registrationInstanceSimple = new RegistrationInstanceSimple(serviceClass);
        simpleCollection.add(registrationInstanceSimple);
        return registrationInstanceSimple;
    }


    protected List<RegistrationInstanceSimple> registry() {
        return simpleCollection;
    }

    public List<RegistrationModuleAnnotation> modules() {
        return modulesCollection;
    }

    public void register(RegistrationModuleAnnotation moduleAnnotation) {
        modulesCollection.add(moduleAnnotation);
    }
}

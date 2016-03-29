package org.hrodberaht.injection.spi.module;

import org.hrodberaht.injection.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;

/**
 * Created by alexbrob on 2016-02-25.
 */
public class CustomInstanceCreatorModule extends RegistrationModuleAnnotation {

    private InstanceCreator instanceCreator;

    public CustomInstanceCreatorModule(InstanceCreator instanceCreator) {
        this.instanceCreator = instanceCreator;
    }

    @Override
    public void registrations() {

    }

    @Override
    public InstanceCreator getInstanceCreator() {
        return instanceCreator;
    }
}

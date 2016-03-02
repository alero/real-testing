package org.hrodberaht.inject.spi.module;

import org.hrodberaht.inject.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.inject.register.RegistrationModuleAnnotation;

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

package org.hrodberaht.inject.testservices.regmodules;

import org.hrodberaht.inject.testservices.annotated.Car;
import org.hrodberaht.inject.testservices.annotated.Spare;
import org.hrodberaht.inject.testservices.annotated.SpareTire;
import org.hrodberaht.inject.testservices.annotated.SpareVindShield;
import org.hrodberaht.inject.testservices.annotated.TestDriver;
import org.hrodberaht.inject.testservices.annotated.Tire;
import org.hrodberaht.inject.testservices.annotated.VindShield;
import org.hrodberaht.inject.testservices.annotated.Volvo;
import org.hrodberaht.injection.internal.ScopeContainer;
import org.hrodberaht.injection.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * 2010-aug-01 16:32:48
 * @version 1.0
 * @since 1.0
 */
public class RegisterModuleWithInstanceFactoryAnnotated extends RegistrationModuleAnnotation {

    @Override
    public void registrations() {
        InjectionFinder injectionFinder = new CustomInjectionPointFinder();
        registerInjectionFinder(injectionFinder);

        register(Car.class).with(Volvo.class);
        register(Tire.class).annotated(Spare.class).with(SpareTire.class);
        register(VindShield.class).annotated(Spare.class).with(SpareVindShield.class);
        register(TestDriver.class).scopeAs(ScopeContainer.Scope.SINGLETON).with(TestDriver.class);
    }


}

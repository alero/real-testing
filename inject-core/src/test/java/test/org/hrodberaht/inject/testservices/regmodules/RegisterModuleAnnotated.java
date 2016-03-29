package test.org.hrodberaht.inject.testservices.regmodules;

import org.hrodberaht.injection.ScopeContainer;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import test.org.hrodberaht.inject.testservices.annotated.Car;
import test.org.hrodberaht.inject.testservices.annotated.Spare;
import test.org.hrodberaht.inject.testservices.annotated.SpareTire;
import test.org.hrodberaht.inject.testservices.annotated.SpareVindShield;
import test.org.hrodberaht.inject.testservices.annotated.TestDriver;
import test.org.hrodberaht.inject.testservices.annotated.Tire;
import test.org.hrodberaht.inject.testservices.annotated.VindShield;
import test.org.hrodberaht.inject.testservices.annotated.Volvo;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-aug-01 16:32:48
 * @version 1.0
 * @since 1.0
 */
public class RegisterModuleAnnotated extends RegistrationModuleAnnotation {

    @Override
    public void registrations() {
        register(Car.class).with(Volvo.class);
        register(Tire.class).annotated(Spare.class).with(SpareTire.class);
        register(VindShield.class).annotated(Spare.class).with(SpareVindShield.class);
        register(TestDriver.class).scopeAs(ScopeContainer.Scope.SINGLETON).with(TestDriver.class);
    }
}

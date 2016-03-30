package test.org.hrodberaht.inject.testservices.regmodules;

import org.hrodberaht.injection.internal.ScopeContainer;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import test.org.hrodberaht.inject.testservices.annotated.*;

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

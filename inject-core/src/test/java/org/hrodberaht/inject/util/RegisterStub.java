package org.hrodberaht.inject.util;

import org.hrodberaht.inject.testservices.annotated.Spare;
import org.hrodberaht.inject.testservices.annotated.SpareTire;
import org.hrodberaht.inject.testservices.annotated.Tire;
import org.hrodberaht.injection.internal.InjectionRegisterScan;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-aug-01 17:14:13
 * @version 1.0
 * @since 1.0
 */
public class RegisterStub {

    public static InjectionRegisterScan createAnnotatedScanRegister() {
        InjectionRegisterScan register = new InjectionRegisterScan();
        register.scanPackage("org.hrodberaht.inject.testservices.annotated");
        register.register(Spare.class, Tire.class, SpareTire.class);
        return register;
    }
}

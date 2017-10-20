package org.hrodberaht.inject.annotation;

import org.hrodberaht.inject.testservices.annotated.Car;
import org.hrodberaht.inject.testservices.annotated.Volvo;
import org.hrodberaht.inject.testservices.annotated.VolvoManufacturer;
import org.hrodberaht.inject.testservices.annotated_extra.Manufacturer;
import org.hrodberaht.inject.testservices.annotated_extra.Saab;
import org.hrodberaht.inject.testservices.annotated_extra.SaabManufacturer;
import org.hrodberaht.injection.register.VariableInjectionFactory;

/**
 * Injection Extension JUnit
 *
 * @author Robert Alexandersson
 * 2010-sep-26 21:53:03
 * @version 1.0
 * @since 1.0
 */
public class ManufacturerVariableFactory implements VariableInjectionFactory<Car, Manufacturer> {
    public Class<? extends Car> getInstanceClass(Manufacturer variable) {
        if (variable instanceof SaabManufacturer) {
            return Saab.class;
        }
        if (variable instanceof VolvoManufacturer) {
            return Volvo.class;
        }
        return null;
    }

    public Class getInstanceType() {
        return Car.class;
    }
}

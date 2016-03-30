package test.org.hrodberaht.inject.annotation;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.junit.Test;
import test.org.hrodberaht.inject.testservices.annotated.Car;
import test.org.hrodberaht.inject.testservices.annotated.SpareTire;
import test.org.hrodberaht.inject.testservices.annotated.SpareVindShield;
import test.org.hrodberaht.inject.testservices.annotated_extra.CarWrapper;
import test.org.hrodberaht.inject.testservices.annotated_extra.SaabManufacturer;

import static org.junit.Assert.*;

/**
 * Injection Extension JUnit
 *
 * @author Robert Alexandersson
 *         2010-sep-26 21:32:07
 * @version 1.0
 * @since 1.0
 */
public class AnnotationVariableProviderUnitT {

    @Test
    public void testSimpleVariableProvider() {

        InjectionRegisterModule registerJava = AnnotationContainerUtil.prepareVolvoRegister();
        registerJava.register(new RegistrationModuleAnnotation() {
            @Override
            public void registrations() {
                register(Car.class).withVariableFactory(new ManufacturerVariableFactory());
            }
        });
        InjectContainer container = registerJava.getContainer();

        Car car = container.get(Car.class, new SaabManufacturer());

        assertSaabCar(car);


    }

    @Test
    public void testVariableProviderInterface() {

        InjectionRegisterModule registerJava = AnnotationContainerUtil.prepareVolvoRegister();
        registerJava.register(new RegistrationModuleAnnotation() {
            @Override
            public void registrations() {
                register(Car.class).withVariableFactory(new ManufacturerVariableFactory());
            }
        });
        InjectContainer container = registerJava.getContainer();

        CarWrapper carWrapper = container.get(CarWrapper.class);

        Car car = carWrapper.getCar(new SaabManufacturer());

        assertSaabCar(car);


    }

    private void assertSaabCar(Car car) {
        assertEquals("Saab", car.brand());

        assertTrue(car.getSpareTire() instanceof SpareTire);
        assertTrue(car.getSpareVindShield() instanceof SpareVindShield);
        assertNotNull(car.getDriver());
    }


}

package org.hrodberaht.inject;

import org.hrodberaht.inject.testservices.annotated.Car;
import org.hrodberaht.inject.testservices.annotated.Volvo;
import org.hrodberaht.inject.util.RegisterStub;
import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.internal.InjectionRegisterScan;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * 2010-jun-06 03:14:13
 * @version 1.0
 * @since 1.0
 */
public class ScopeThreadUnitT {

    private boolean successful = false;

    @Before
    public void init() {
        successful = false;
    }

    @Test
    public void testThreadScope() {
        InjectionRegisterScan register = RegisterStub.createAnnotatedScanRegister();
        InjectContainer container = register.getContainer();
        register.printRegistration(System.out);
        Volvo aCar = (Volvo) container.get(Car.class);

        Volvo aSecondCar = (Volvo) container.get(Car.class);

        // Very simple test that the driver is cached during this thread.
        assertTrue(aCar.getDriver() == aSecondCar.getDriver());
        assertFalse(aCar.getSpareTire() == aSecondCar.getSpareTire());


    }

    @Test
    public void testDifferentThreadScope() {
        InjectionRegisterScan register = RegisterStub.createAnnotatedScanRegister();
        final InjectContainer container = register.getContainer();
        final Volvo aCar = (Volvo) container.get(Car.class);


        Thread thread = new Thread() {
            @Override
            public void run() {
                Volvo aSecondCar = (Volvo) container.get(Car.class);
                assertFalse(aCar.getDriver() == aSecondCar.getDriver());
                assertFalse(aCar.getSpareTire() == aSecondCar.getSpareTire());
                successful = true;
            }
        };
        thread.start();
        waitForIt(thread);
        assertTrue("Threaded test failed", successful);

    }

    @Test
    public void testInheritedThreadScope() {
        InjectionRegisterScan register = RegisterStub.createAnnotatedScanRegister();

        final InjectContainer container = register.getContainer();
        final Volvo aCar = (Volvo) container.get(Car.class);

        Thread thread = new Thread() {
            @Override
            public void run() {
                Volvo aSecondCar = (Volvo) container.get(Car.class);
                assertTrue(aCar.getDriverManager() == aSecondCar.getDriverManager());
                assertFalse(aCar.getSpareTire() == aSecondCar.getSpareTire());
                successful = true;
            }
        };
        thread.start();
        waitForIt(thread);
        // Very simple test that the driver is cached during this thread.

        assertTrue("Threaded test failed", successful);


    }


    private void waitForIt(Thread thread) {
        try {
            while (thread.isAlive())
                Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

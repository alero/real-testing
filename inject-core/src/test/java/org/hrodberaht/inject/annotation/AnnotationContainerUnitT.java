/*
 * Copyright (c) 2017 org.hrodberaht
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hrodberaht.inject.annotation;


import org.hrodberaht.inject.testservices.annotated.Car;
import org.hrodberaht.inject.testservices.annotated.Spare;
import org.hrodberaht.inject.testservices.annotated.SpareTire;
import org.hrodberaht.inject.testservices.annotated.SpareVindShield;
import org.hrodberaht.inject.testservices.annotated.SpecialSpareTire;
import org.hrodberaht.inject.testservices.annotated.TestDriverManager;
import org.hrodberaht.inject.testservices.annotated.Tire;
import org.hrodberaht.inject.testservices.annotated.VindShield;
import org.hrodberaht.inject.testservices.annotated.Volvo;
import org.hrodberaht.inject.testservices.annotated.VolvoFactory;
import org.hrodberaht.inject.testservices.interfaces.TestingService;
import org.hrodberaht.inject.testservices.interfaces.TestingServiceInner;
import org.hrodberaht.inject.testservices.interfaces.TestingServiceInnerInterface;
import org.hrodberaht.inject.testservices.interfaces.TestingServiceInterface;
import org.hrodberaht.inject.testservices.regmodules.CustomInjectionPointFinder;
import org.hrodberaht.inject.testservices.regmodules.OverridesRegisterModuleAnnotated;
import org.hrodberaht.inject.testservices.regmodules.RegisterModuleAnnotated;
import org.hrodberaht.injection.core.InjectContainer;
import org.hrodberaht.injection.core.internal.InjectionRegisterModule;
import org.hrodberaht.injection.core.internal.InjectionRegisterScan;
import org.hrodberaht.injection.core.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.core.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.injection.core.register.InjectionRegister;
import org.hrodberaht.injection.core.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.core.spi.module.CustomInjectionPointFinderModule;
import org.hrodberaht.injection.core.spi.module.CustomInstanceCreatorModule;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * 2010-maj-29 17:02:46
 * @version 1.0
 * @since 1.0
 */
public class AnnotationContainerUnitT {


    @Test
    public void testFindAnnotatedWithForTwoDifferentServices() {

        InjectionRegister registerJava = AnnotationContainerUtil.prepareVolvoRegister();
        InjectContainer container = registerJava.getContainer();
        Tire spareTire = container.get(Tire.class, Spare.class);
        VindShield vindShield = container.get(VindShield.class, Spare.class);

        assertTrue(spareTire instanceof SpareTire);

        assertTrue(vindShield instanceof SpareVindShield);
    }

    @Test
    public void testInjectDependencies() {
        InjectionRegister registerJava = AnnotationContainerUtil.prepareVolvoRegister();

        InjectContainer container = registerJava.getContainer();

        Volvo aVolvo = new Volvo();
        container.injectDependencies(aVolvo);

        assertTrue(aVolvo.getSpareTire() instanceof SpareTire);

        assertTrue(aVolvo.getVindShield() instanceof VindShield);
    }

    @Test
    public void testOverrideSupport() {

        InjectionRegister registerJava = AnnotationContainerUtil.prepareVolvoRegister();
        registerJava.overrideRegister(Spare.class, Tire.class, Tire.class);

        InjectContainer container = registerJava.getContainer();
        Tire spareTire = container.get(Tire.class, Spare.class);
        VindShield vindShield = container.get(VindShield.class, Spare.class);

        assertFalse(spareTire instanceof SpareTire);

        assertTrue(vindShield instanceof SpareVindShield);
    }

    @Test
    public void testOverrideModuleSupport() {

        InjectionRegisterModule registerJava = new InjectionRegisterModule();

        // This is normally done in a more dynamic way.
        // Its intended to support advances inheritance support for registrations

        // The regular (default/basic) registration
        registerJava.register(new RegisterModuleAnnotated());

        // The override (of a default) registration
        registerJava.register(new OverridesRegisterModuleAnnotated());

        registerJava.printRegistration(System.out);

        InjectContainer container = registerJava.getContainer();
        Tire spareTire = container.get(Tire.class, Spare.class);
        VindShield vindShield = container.get(VindShield.class, Spare.class);

        assertFalse(spareTire instanceof SpareTire);

        assertTrue(vindShield instanceof SpareVindShield);

    }

    @Test
    public void testOverrideModuleScanSupport() {

        // This is normally done in a more dynamic way.
        // Its intended to give advanced inheritance like support for registrations

        // The regular (default/basic) registration
        InjectionRegisterScan registerScan = new InjectionRegisterScan();
        registerScan.scanPackage("org.hrodberaht.inject.testservices.annotated");
        InjectionRegisterModule registerJava = new InjectionRegisterModule(registerScan);
        // The override (of a default) registration
        registerJava.register(new RegisterModuleAnnotated());

        registerJava.printRegistration(System.out);

        InjectContainer container = registerJava.getContainer();
        Car car = container.get(Car.class);


        assertTrue(car.getSpareTire() instanceof SpareTire);

        assertTrue(car.getSpareVindShield() instanceof SpareVindShield);

    }

    @Test
    public void testProviderInjection() {

        InjectionRegister registerJava = AnnotationContainerUtil.prepareVolvoRegister();
        InjectContainer container = registerJava.getContainer();
        TestDriverManager manager = container.get(TestDriverManager.class);


        assertTrue(manager.getCar() instanceof Volvo);
        assertTrue(manager.getTire() instanceof SpareTire);

    }


    @Test
    public void testPostConstruct() {

        InjectionRegister registerJava = AnnotationContainerUtil.prepareVolvoRegister();
        InjectContainer container = registerJava.getContainer();
        Volvo aCar = (Volvo) container.get(Car.class);

        assertEquals("Initialized", aCar.getInitText());

    }

    @Test
    public void testCustomInstanceCreator() {
        final Tire specialTire = new Tire();

        InjectionRegisterModule registerJava = AnnotationContainerUtil.prepareVolvoRegister();
        InstanceCreator instanceCreator = (constructor, parameters) -> {
            try {
                if (constructor.getDeclaringClass().isAssignableFrom(Tire.class)
                        && (parameters == null || parameters.length == 0)) {
                    return specialTire;
                }
                return constructor.newInstance(parameters);
            } catch (InstantiationException e) {

            } catch (IllegalAccessException e) {

            } catch (InvocationTargetException e) {

            }
            return null;
        };
        registerJava.register(new CustomInstanceCreatorModule(instanceCreator));


        InjectContainer container = registerJava.getContainer();


        Volvo aCar = (Volvo) container.get(Car.class);
        assertEquals("Initialized", aCar.getInitText());
        assertTrue(specialTire == aCar.getBackLeft());

    }

    @Test
    public void testCustomAnnotationsSupport() {

        InjectionFinder finder = new CustomInjectionPointFinder();


        InjectionRegisterModule registerJava = AnnotationContainerUtil.prepareVolvoRegister();
        registerJava.register(new CustomInjectionPointFinderModule(finder));
        InjectContainer container = registerJava.getContainer();

        Volvo aCar = (Volvo) container.get(Car.class);
        assertNotNull("getSpecialInjectField is null", aCar.getSpecialInjectField());
        assertNotNull("getSpecialInjectMethod is null", aCar.getSpecialInjectMethod());
        assertEquals("Initialized Text", aCar.getInitText());
        assertEquals("Initialized special", aCar.getDriverManager().getInitTextSpecial());


    }

    @Test
    public void testCustomAnnotationsSupportUsingNonStaticSupport() {


        InjectionRegister registerJava = AnnotationContainerUtil.prepareVolvoRegisterWithFinder();
        InjectContainer container = registerJava.getContainer();

        Volvo aCar = (Volvo) container.get(Car.class);
        assertNotNull("getSpecialInjectField is null", aCar.getSpecialInjectField());
        assertNotNull("getSpecialInjectMethod is null", aCar.getSpecialInjectMethod());
        assertEquals("Initialized Text", aCar.getInitText());
        assertEquals("Initialized special", aCar.getDriverManager().getInitTextSpecial());


    }

    @Test
    public void testInstanceRegisterSupport() {

        // The regular (default/basic) registration
        InjectionRegisterModule registerJava = new InjectionRegisterModule();
        registerJava.register(new RegisterModuleAnnotated());
        // The override (of a default) registration
        final SpareTire aTire = new SpecialSpareTire("Goodyear");
        registerJava.register(
                new RegistrationModuleAnnotation() {
                    @Override
                    public void registrations() {
                        register(Tire.class).annotated(Spare.class).withInstance(aTire);
                    }
                }
        );


        InjectContainer container = registerJava.getContainer();
        Car car = container.get(Car.class);

        assertTrue(car.getSpareTire() instanceof SpareTire);
        assertEquals("Goodyear", car.getSpareTire().getBrand());
        assertTrue(car.getSpareVindShield() instanceof SpareVindShield);

    }

    @Test
    public void testFactoryRegisterSupport() {

        // The regular (default/basic) registration
        InjectionRegisterModule registerJava = new InjectionRegisterModule();
        registerJava.register(new RegisterModuleAnnotated());
        // The override (of a default) registration
        final VolvoFactory aFactory = new VolvoFactory();
        registerJava.register(
                new RegistrationModuleAnnotation() {
                    @Override
                    public void registrations() {
                        register(Car.class).withFactory(aFactory);
                    }
                }
        );


        InjectContainer container = registerJava.getContainer();
        Car car = container.get(Car.class);

        // After the factory created the Car it will be deeper injected.
        assertTrue(car.getSpareTire() instanceof SpareTire);
        assertTrue(car.getSpareVindShield() instanceof SpareVindShield);

        // This is information set by the factory though         
        assertEquals("Made from factory", ((Volvo) car).getInformation());

    }

    @Test
    public void testDeepInterfaceLookup() {

        // The regular (default/basic) registration
        InjectionRegisterModule registerJava = new InjectionRegisterModule();
        // The override (of a default) registration
        registerJava.register(
                new RegistrationModuleAnnotation() {
                    @Override
                    public void registrations() {
                        register(TestingServiceInnerInterface.class).with(TestingServiceInner.class);
                        register(TestingServiceInterface.class).with(TestingService.class);
                    }
                }
        );


        InjectContainer container = registerJava.getContainer();
        TestingServiceInterface testingServiceInterface = container.get(TestingServiceInterface.class);

        // After the factory created the Car it will be deeper injected.
        assertTrue(testingServiceInterface != null);

    }


    @Test
    public void testFactoryProviderRegisterSupport() {

        // The regular (default/basic) registration
        InjectionRegisterModule registerJava = new InjectionRegisterModule();
        registerJava.register(new RegisterModuleAnnotated());
        // The override (of a default) registration
        final VolvoFactory aFactory = new VolvoFactory();
        registerJava.register(
                new RegistrationModuleAnnotation() {
                    @Override
                    public void registrations() {
                        register(Car.class).withFactory(aFactory);
                    }
                }
        );

        InjectContainer container = registerJava.getContainer();
        TestDriverManager driverManager = container.get(TestDriverManager.class);

        // After the factory created the Car it will be deeper injected.
        assertTrue(driverManager.getCar() instanceof Volvo);
        assertTrue(driverManager.getTire() instanceof SpareTire);

        // This is information set by the factory though
        assertEquals("Made from factory", ((Volvo) driverManager.getCar()).getInformation());

    }
}
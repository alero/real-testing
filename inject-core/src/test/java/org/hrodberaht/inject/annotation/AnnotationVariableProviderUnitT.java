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
import org.hrodberaht.inject.testservices.annotated.SpareTire;
import org.hrodberaht.inject.testservices.annotated.SpareVindShield;
import org.hrodberaht.inject.testservices.annotated_extra.CarWrapper;
import org.hrodberaht.inject.testservices.annotated_extra.SaabManufacturer;
import org.hrodberaht.injection.core.InjectContainer;
import org.hrodberaht.injection.core.internal.InjectionRegisterModule;
import org.hrodberaht.injection.core.register.RegistrationModuleAnnotation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Injection Extension JUnit
 *
 * @author Robert Alexandersson
 * 2010-sep-26 21:32:07
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

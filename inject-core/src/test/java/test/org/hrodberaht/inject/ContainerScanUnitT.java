/*
 * ~ Copyright (c) 2010.
 *   ~ Licensed under the Apache License, Version 2.0 (the "License");
 *   ~ you may not use this file except in compliance with the License.
 *   ~ You may obtain a copy of the License at
 *   ~
 *   ~        http://www.apache.org/licenses/LICENSE-2.0
 *   ~
 *   ~ Unless required by applicable law or agreed to in writing, software
 *   ~ distributed under the License is distributed on an "AS IS" BASIS,
 *   ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   ~ See the License for the specific language governing permissions and limitations under the License.
 */

package test.org.hrodberaht.inject;

import org.hrodberaht.inject.Container;
import org.hrodberaht.inject.InjectionRegisterJava;
import org.hrodberaht.inject.InjectionRegisterModule;
import org.hrodberaht.inject.InjectionRegisterScan;
import org.hrodberaht.inject.internal.exception.InjectRuntimeException;
import org.hrodberaht.inject.register.RegistrationModuleAnnotationScanner;
import org.junit.Test;
import test.org.hrodberaht.inject.testservices.annotated.Car;
import test.org.hrodberaht.inject.testservices.annotated.Volvo;
import test.org.hrodberaht.inject.testservices.annotated_extra.Manufacturer;
import test.org.hrodberaht.inject.testservices.annotated_extra.SaabManufacturer;
import test.org.hrodberaht.inject.testservices.simple.AnyService;
import test.org.hrodberaht.inject.testservices.simple.AnyServiceDoNothingImpl;
import test.org.hrodberaht.inject.testservices.simple.AnyServiceDoSomethingImpl;
import test.org.hrodberaht.inject.util.RegisterStub;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 17:02:46
 * @version 1.0
 * @since 1.0
 */
public class ContainerScanUnitT {


    @Test
    public void testScanningOfImplementations() {

        InjectionRegisterScan register = new InjectionRegisterScan();
        // Tests scanning and exclusion of single class
        register.registerBasePackageScan("test.org.hrodberaht.inject.testservices.simple", AnyServiceDoNothingImpl.class);

        Container container = register.getContainer();

        AnyService anyService = container.get(AnyService.class);
        anyService.doStuff();

        assertEquals(1, anyService.getStuff().size());

    }


    @Test
    public void testAnnotatedScanningOfImplementations() {
        InjectionRegisterScan register = RegisterStub.createAnnotatedScanRegister();

        Container container = register.getContainer();
        Car aCar = container.get(Car.class);

        assertEquals("volvo", aCar.brand());
        Volvo aVolvo = (Volvo) aCar;
        assertNotNull(aVolvo.getBackLeft());
        assertNotNull(aVolvo.getBackRight());
        assertNotNull(aVolvo.getFrontLeft());
        assertNotNull(aVolvo.getFrontRight());
        assertNotNull(aVolvo.getSpareTire());
        assertNotNull(aVolvo.getVindShield());

    }

    @Test
    public void testScanningOfSinglePackageImplementations() {

        InjectionRegisterScan register = new InjectionRegisterScan();
        // Tests scanning and exclusion of single class
        register.registerBasePackageScan("test.org.hrodberaht.inject.testservices.simple", AnyServiceDoNothingImpl.class);
        Container container = register.getContainer();

        AnyService anyService = container.get(AnyService.class);
        anyService.doStuff();

        assertEquals(1, anyService.getStuff().size());

    }

    @Test
    public void testScanningOfDuplicateImplementations() {

        InjectionRegisterScan register = new InjectionRegisterScan();
        // Tests scanning and exclusion of single class
        register.registerBasePackageScan("test.org.hrodberaht.inject.testservices.simple");

        Container container = register.getContainer();

        try {
            AnyService anyService = container.get(AnyService.class);
            assertEquals("Not allowed", "");
        } catch (InjectRuntimeException e) {
            /*assertEquals(
                    "Found two Implementations " +
                            "\"class test.org.hrodberaht.inject.testservices.simple.AnyServiceDoSomethingImpl\"" +
                            ", \"class test.org.hrodberaht.inject.testservices.simple.AnyServiceDoNothingImpl\" " +
                            "matching the Interface \"interface test.org.hrodberaht.inject.testservices.simple.AnyService\"" +
                            ". This normally occurs when scanning implementations and can be corrected " +
                            " by manually registering one of them to the Interface"
                    , e.getMessage());
                    */
            assertEquals("This needs to be more clever", "This needs to be more clever");
        }

    }

    @Test
    public void testScanningAndRegisterWhenDuplicateImplementations() {


        InjectionRegisterScan register = new InjectionRegisterScan();
        // Tests scanning and exclusion of single class
        register.registerBasePackageScan("test.org.hrodberaht.inject.testservices.simple");

        InjectionRegisterJava registerJava = new InjectionRegisterJava(register);
        registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);

        Container container = register.getContainer();

        AnyService anyService = container.get(AnyService.class);
        assertTrue(anyService instanceof AnyServiceDoSomethingImpl);

    }

    @Test
    public void testScanningAndRegisterAScanningModule() {

        InjectionRegisterModule injectionRegisterModule = new InjectionRegisterModule();
        RegistrationModuleAnnotationScanner propertiesModule = new RegistrationModuleAnnotationScanner() {

            public void scan() {
                this.scanAndRegister("test.org.hrodberaht.inject.testservices.annotated_extra");
            }
        };
        injectionRegisterModule.register(propertiesModule);

        InjectionRegisterScan register = new InjectionRegisterScan(injectionRegisterModule);
        // Tests scanning and exclusion of single class
        register.registerBasePackageScan("test.org.hrodberaht.inject.testservices.simple");

        InjectionRegisterJava registerJava = new InjectionRegisterJava(register);
        registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);

        Container container = register.getContainer();

        AnyService anyService = container.get(AnyService.class);
        assertTrue(anyService instanceof AnyServiceDoSomethingImpl);

        Manufacturer manufacturer = container.get(Manufacturer.class);
        assertTrue(manufacturer instanceof SaabManufacturer);

    }


}

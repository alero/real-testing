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

package test.org.hrodberaht.inject.annotation;

import org.hrodberaht.inject.InjectionRegisterModule;
import org.hrodberaht.inject.InjectionRegisterScan;
import org.hrodberaht.inject.register.InjectionRegister;
import test.org.hrodberaht.inject.testservices.annotated.SpareTire;
import test.org.hrodberaht.inject.testservices.annotated.SpareVindShield;
import test.org.hrodberaht.inject.testservices.annotated.TestDriverManager;
import test.org.hrodberaht.inject.testservices.annotated.Volvo;
import test.org.hrodberaht.inject.testservices.regmodules.RegisterModuleAnnotated;
import test.org.hrodberaht.inject.testservices.regmodules.RegisterModuleWithInstanceFactoryAnnotated;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 15:39:51
 * @version 1.0
 * @since 1.0
 */
public class AnnotationContainerUtil {

    public static InjectionRegisterModule prepareVolvoRegister() {
        InjectionRegisterModule registerJava = new InjectionRegisterModule();
        registerJava.register(new RegisterModuleAnnotated());
        return registerJava;
    }

    public static InjectionRegisterModule prepareLargeVolvoRegister() {
        InjectionRegisterModule registerJava = new InjectionRegisterModule();
        registerJava.register(new RegisterModuleAnnotated());
        InjectionRegisterScan registerScan = new InjectionRegisterScan(registerJava);
        registerScan.scanPackage("test.org.hrodberaht.inject.testservices.largepackage");
        return registerJava;
    }

    public static void assertVolvo(Volvo car) {

        assertNotNull(car.getSpareTire());
        assertNotNull(car.getSpareVindShield());
        assertNotNull(car.getVindShield());
        assertNotNull(car.getDriver());
        assertNotNull(car.getBackLeft());
        assertNotNull(car.getBackRight());
        assertNotNull(car.getFrontLeft());
        assertNotNull(car.getFrontRight());

        assertTrue(car.getSpareTire() instanceof SpareTire);
        assertTrue(car.getSpareVindShield() instanceof SpareVindShield);

        TestDriverManager manager = car.getDriverManager();
        assertTrue(manager.getCar() instanceof Volvo);
        assertTrue(manager.getTire() instanceof SpareTire);
    }

    public static InjectionRegister prepareVolvoRegisterWithFinder() {
        InjectionRegisterModule registerJava = new InjectionRegisterModule();
        registerJava.register(new RegisterModuleWithInstanceFactoryAnnotated());
        return registerJava;
    }
}

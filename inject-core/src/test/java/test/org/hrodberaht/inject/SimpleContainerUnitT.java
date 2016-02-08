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
import org.hrodberaht.inject.ScopeContainer;
import org.hrodberaht.inject.SimpleInjection;
import org.hrodberaht.inject.internal.exception.DuplicateRegistrationException;
import org.hrodberaht.inject.internal.exception.InjectRuntimeException;
import org.hrodberaht.inject.register.RegistrationModuleSimple;
import org.junit.Before;
import org.junit.Test;
import test.org.hrodberaht.inject.testservices.simple.AnyService;
import test.org.hrodberaht.inject.testservices.simple.AnyServiceDoNothingImpl;
import test.org.hrodberaht.inject.testservices.simple.AnyServiceDoSomethingImpl;
import test.org.hrodberaht.inject.testservices.simple.DoNothing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class SimpleContainerUnitT {

    @Before
    public void init() {

    }

    @Test
    public void testNothingRegistered() {
        Container container = new InjectionRegisterJava().activateContainerSimple().getContainer();
        try {
            AnyService anyService = container.get(AnyService.class);
            assertEquals(null, "Should not be called");
        } catch (InjectRuntimeException e) {
            assertEquals(
                    "Service interface "
                            + AnyService.class.getName() +
                            " not registered in SimpleInjection"
                    , e.getMessage());
        }
    }

    @Test
    public void testNothingServiceWrapping() {
        Container container = registerSingle(AnyService.class, AnyServiceDoNothingImpl.class);

        AnyService anyService = container.get(AnyService.class);

        assertEquals(null, anyService.getStuff());
    }

    @Test(expected = RuntimeException.class)
    public void testNothingServiceError() {
        Container container = registerSingle(AnyService.class, AnyServiceDoNothingImpl.class);

        AnyService anyService = container.get(AnyService.class);

        anyService.doStuff();
    }


    @Test
    public void testNamedNothingServiceWrapping() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.activateContainerSimple();
        registerJava.register("myAnyService", AnyService.class, AnyServiceDoNothingImpl.class);
        Container container = registerJava.getContainer();

        AnyService anyService = container.get(AnyService.class, "myAnyService");

        assertEquals(null, anyService.getStuff());
    }

    @Test
    public void testSomethingServiceWrapping() {
        Container injection = registerSingle(AnyService.class, AnyServiceDoSomethingImpl.class);
        AnyService anyService = injection.get(AnyService.class);

        anyService.doStuff();

        assertEquals(1, anyService.getStuff().size());
    }

    @Test
    public void testSomethingServiceNewObjectSupport() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.activateContainerSimple();
        registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class, SimpleInjection.Scope.SINGLETON);
        ScopeContainer container = registerJava.getScopedContainer();

        AnyService anyService = container.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

        AnyService anyServiceSingleton = container.get(AnyService.class);
        assertEquals(1, anyServiceSingleton.getStuff().size());

        AnyService anyServiceNew = container.getNew(AnyService.class);
        assertEquals(0, anyServiceNew.getStuff().size());
    }

    @Test
    public void testSomethingServiceSingletonObjectSupport() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.activateContainerSimple();
        registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);
        ScopeContainer container = registerJava.getScopedContainer();

        AnyService anyService = container.getSingleton(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

        AnyService anyServiceSingleton = container.getSingleton(AnyService.class);
        assertEquals(1, anyServiceSingleton.getStuff().size());

        AnyService anyServiceNew = container.get(AnyService.class);
        assertEquals(0, anyServiceNew.getStuff().size());
    }

    @Test
    public void testReRegisterSupport() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.activateContainerSimple();
        registerJava.register(AnyService.class, AnyServiceDoNothingImpl.class);
        registerJava.overrideRegister(AnyService.class, AnyServiceDoSomethingImpl.class);

        Container container = registerJava.getContainer();

        AnyService anyService = container.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

    }

    @Test
    public void testDefaultRegisterSupport() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.activateContainerSimple();
        registerJava.registerDefault(AnyService.class, AnyServiceDoNothingImpl.class);
        registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);
        Container container = registerJava.getContainer();

        AnyService anyService = container.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

    }

    @Test(expected = DuplicateRegistrationException.class)
    public void testFinalRegisterSupport() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.activateContainerSimple();
        registerJava.finalRegister(AnyService.class, AnyServiceDoNothingImpl.class);
        registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);

    }

    @Test
    public void testNormalRegisterFail() {

        try {
            InjectionRegisterJava registerJava = new InjectionRegisterJava();
            registerJava.activateContainerSimple();
            registerJava.register(AnyService.class, AnyServiceDoNothingImpl.class);
            registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);
            assertEquals("Not suppose to execute this", "So fail");
        } catch (DuplicateRegistrationException e) {
            assertEquals(
                    "a Service for serviceDefinition \"interface " + AnyService.class.getName() +
                            "\" is already registered, to override register please use overrideRegister method"
                    , e.getMessage());
        }


    }

    @Test
    public void testRegisterModule() {

        InjectionRegisterModule registerJava = new InjectionRegisterModule();
        registerJava.activateContainerSimple();
        registerJava.register(new RegistrationModuleSimple() {
            public void registrations() {
                register(AnyService.class).annotated(DoNothing.class).with(AnyServiceDoNothingImpl.class);
                register(AnyService.class).with(AnyServiceDoSomethingImpl.class);
            }
        });
        Container container = registerJava.getContainer();

        AnyService anyService = container.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());


        AnyService anyNothingService = container.get(AnyService.class, DoNothing.class);
        assertNull(anyNothingService.getStuff());

    }

    @Test
    public void testAdvancedRegisterModule() {

        InjectionRegisterModule registerJava = new InjectionRegisterModule();
        registerJava.activateContainerSimple();
        registerJava.register(new RegistrationModuleSimple() {
            public void registrations() {
                register(AnyService.class)
                        .annotated(DoNothing.class)
                        .scopeAs(ScopeContainer.Scope.SINGLETON)
                        .with(AnyServiceDoNothingImpl.class);

                register(AnyService.class)
                        .registerTypeAs(SimpleInjection.RegisterType.FINAL)
                        .with(AnyServiceDoSomethingImpl.class);
            }
        });

        try {
            registerJava.register(AnyService.class, AnyServiceDoNothingImpl.class);
        } catch (DuplicateRegistrationException e) {
            assertEquals(
                    "a FINAL Service for serviceDefinition \"interface "
                            + AnyService.class.getName() + "\" is already registered, " +
                            "can not perform override registration"
                    , e.getMessage());
        }


        Container container = registerJava.getContainer();

        AnyService anyService = container.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());


        AnyService anyNothingService = container.get(AnyService.class, DoNothing.class);
        assertNull(anyNothingService.getStuff());

        AnyService anyNothingService2 = container.get(AnyService.class, DoNothing.class);
        // Verify singleton
        assertEquals(anyNothingService, anyNothingService2);

    }


    private Container registerSingle(Class serviceDefinition, Class aService) {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.activateContainerSimple();
        registerJava.register(serviceDefinition, aService);
        Container injection = registerJava.getContainer();
        return injection;
    }

}
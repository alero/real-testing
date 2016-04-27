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

package org.hrodberaht.inject;

import org.hrodberaht.inject.testservices.simple.AnyService;
import org.hrodberaht.inject.testservices.simple.AnyServiceDoNothingImpl;
import org.hrodberaht.inject.testservices.simple.AnyServiceDoSomethingImpl;
import org.hrodberaht.inject.testservices.simple.DoNothing;
import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.internal.InjectionContainerManager;
import org.hrodberaht.injection.internal.InjectionRegisterJava;
import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.internal.ScopeContainer;
import org.hrodberaht.injection.internal.exception.DuplicateRegistrationException;
import org.hrodberaht.injection.internal.exception.InjectRuntimeException;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.junit.Before;
import org.junit.Test;

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
        InjectContainer container = new InjectionRegisterJava().getContainer();
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
        InjectContainer container = registerSingle(AnyService.class, AnyServiceDoNothingImpl.class);

        AnyService anyService = container.get(AnyService.class);

        assertEquals(null, anyService.getStuff());
    }

    @Test(expected = RuntimeException.class)
    public void testNothingServiceError() {
        InjectContainer container = registerSingle(AnyService.class, AnyServiceDoNothingImpl.class);

        AnyService anyService = container.get(AnyService.class);

        anyService.doStuff();
    }


    @Test
    public void testNamedNothingServiceWrapping() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.register("myAnyService", AnyService.class, AnyServiceDoNothingImpl.class);
        InjectContainer container = registerJava.getContainer();

        AnyService anyService = container.get(AnyService.class, "myAnyService");

        assertEquals(null, anyService.getStuff());
    }

    @Test
    public void testSomethingServiceWrapping() {
        InjectContainer container = registerSingle(AnyService.class, AnyServiceDoSomethingImpl.class);
        AnyService anyService = container.get(AnyService.class);

        anyService.doStuff();

        assertEquals(1, anyService.getStuff().size());
    }

    @Test
    public void testSomethingServiceNewObjectSupport() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class, InjectionContainerManager.Scope.SINGLETON);
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
        registerJava.register(AnyService.class, AnyServiceDoNothingImpl.class);
        registerJava.overrideRegister(AnyService.class, AnyServiceDoSomethingImpl.class);

        InjectContainer container = registerJava.getContainer();

        AnyService anyService = container.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

    }

    @Test
    public void testDefaultRegisterSupport() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.registerDefault(AnyService.class, AnyServiceDoNothingImpl.class);
        registerJava.overrideRegister(AnyService.class, AnyServiceDoSomethingImpl.class);
        InjectContainer container = registerJava.getContainer();

        AnyService anyService = container.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

    }

    @Test
    public void testOverrideRegisterAfterUsageSupport() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.register(AnyService.class, AnyServiceDoNothingImpl.class);

        InjectContainer container = registerJava.getContainer();
        AnyService anyService = container.get(AnyService.class);
        assertEquals(null, anyService.getStuff());

        registerJava.overrideRegister(AnyService.class, AnyServiceDoSomethingImpl.class);
        container = registerJava.getContainer();

        anyService = container.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

    }

    @Test(expected = DuplicateRegistrationException.class)
    public void testFinalRegisterSupport() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.finalRegister(AnyService.class, AnyServiceDoNothingImpl.class);
        registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);

    }

    @Test
    public void testNormalRegisterFail() {

        try {
            InjectionRegisterJava registerJava = new InjectionRegisterJava();
            registerJava.register(AnyService.class, AnyServiceDoNothingImpl.class);
            registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);
            assertEquals("Not suppose to execute this", "So fail");
        } catch (DuplicateRegistrationException e) {
            assertEquals(
                    "a existing Service for serviceDefinition \"interface " + AnyService.class.getName() +
                            "\" is already registered, to override register please use overrideRegister method"
                    , e.getMessage());
        }


    }

    @Test
    public void testRegisterModule() {

        InjectionRegisterModule registerJava = new InjectionRegisterModule();
        registerJava.register(new RegistrationModuleAnnotation() {
            public void registrations() {
                register(AnyService.class).annotated(DoNothing.class).with(AnyServiceDoNothingImpl.class);
                register(AnyService.class).with(AnyServiceDoSomethingImpl.class);
            }
        });
        InjectContainer container = registerJava.getContainer();

        AnyService anyService = container.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());


        AnyService anyNothingService = container.get(AnyService.class, DoNothing.class);
        assertNull(anyNothingService.getStuff());

    }

    @Test
    public void testAdvancedRegisterModule() {

        InjectionRegisterModule registerJava = new InjectionRegisterModule();
        registerJava.register(new RegistrationModuleAnnotation() {
            public void registrations() {
                register(AnyService.class)
                        .annotated(DoNothing.class)
                        .scopeAs(ScopeContainer.Scope.SINGLETON)
                        .with(AnyServiceDoNothingImpl.class);

                register(AnyService.class)
                        .registerTypeAs(InjectionContainerManager.RegisterType.FINAL)
                        .with(AnyServiceDoSomethingImpl.class);
            }
        });

        try {
            registerJava.overrideRegister(AnyService.class, AnyServiceDoNothingImpl.class);
            assertEquals("Not allowed to reach this", null);
        } catch (DuplicateRegistrationException e) {
            assertEquals(
                    "a existing FINAL Service for serviceDefinition \"interface "
                            + AnyService.class.getName() + "\" is already registered, " +
                            "can not perform override registration"
                    , e.getMessage());
        }


        InjectContainer container = registerJava.getContainer();

        AnyService anyService = container.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());


        AnyService anyNothingService = container.get(AnyService.class, DoNothing.class);
        assertNull(anyNothingService.getStuff());

        AnyService anyNothingService2 = container.get(AnyService.class, DoNothing.class);
        // Verify singleton
        assertEquals(anyNothingService, anyNothingService2);

    }


    private InjectContainer registerSingle(Class serviceDefinition, Class aService) {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.register(serviceDefinition, aService);
        InjectContainer injection = registerJava.getContainer();
        return injection;
    }

}
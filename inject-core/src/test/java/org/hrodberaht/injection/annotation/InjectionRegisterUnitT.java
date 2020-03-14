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

package org.hrodberaht.injection.annotation;

import org.hrodberaht.injection.testservices.interfaces.TestingService;
import org.hrodberaht.injection.testservices.interfaces.TestingServiceInterface;
import org.hrodberaht.injection.testservices.largepackage.Car;
import org.hrodberaht.injection.testservices.largepackage.Volvo;
import org.hrodberaht.injection.testservices.notregistered.BaseServiceImplementation;
import org.hrodberaht.injection.testservices.simple.AnyService;
import org.hrodberaht.injection.testservices.simple.AnyServiceDoSomethingImpl;
import org.hrodberaht.injection.testservices.sortedinterfaces.ATestingService;
import org.hrodberaht.injection.testservices.sortedinterfaces.ATestingServiceInterface;
import org.hrodberaht.injection.testservices.sortedinterfaces.BTestingServiceInner;
import org.hrodberaht.injection.testservices.sortedinterfaces.BTestingServiceInnerInterface;
import org.hrodberaht.injection.core.InjectContainer;
import org.hrodberaht.injection.core.InjectionRegistryPlain;
import org.hrodberaht.injection.core.Module;
import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by alexbrob on 2016-03-29.
 */
public class InjectionRegisterUnitT {

    @Test
    public void moduleRegistration() throws Exception {

        Module largeScanModule = new Module() {
            @Override
            public void scan() {
                this.scanAndRegister("org.hrodberaht.injection.testservices.largepackage");
            }
        };

        Module scanModule = new Module() {
            @Override
            public void registrations() {
                this.register(AnyService.class).with(AnyServiceDoSomethingImpl.class);
                this.register(ATestingServiceInterface.class).with(ATestingService.class);
                this.register(BTestingServiceInnerInterface.class).with(BTestingServiceInner.class);
            }

            @Override
            public void scan() {
                this.scanAndRegister("org.hrodberaht.injection.testservices.annotated");
            }
        };

        InjectContainer injectionContainer =
                new InjectionRegistryPlain()
                        .register(largeScanModule)
                        .register(scanModule)
                        .getContainer();

        assertTheContainer(injectionContainer);
    }


    @Test
    public void moduleRegistrationLambda() throws Exception {
        InjectContainer injectionContainer =
                new InjectionRegistryBuilder()
                        .scan(() -> "org.hrodberaht.injection.testservices.largepackage")
                        .register(
                                e -> {
                                    e.register(AnyService.class).with(AnyServiceDoSomethingImpl.class);
                                    e.register(ATestingServiceInterface.class).with(ATestingService.class);
                                    e.register(BTestingServiceInnerInterface.class).with(BTestingServiceInner.class);
                                }
                        )
                        .scan(() -> "org.hrodberaht.injection.testservices.annotated")
                        .getContainer();

        assertTheContainer(injectionContainer);

    }

    @Test
    public void moduleMultiScanRegistrationLambda() throws Exception {
        InjectContainer injectionContainer =
                new InjectionRegistryBuilder()
                        .scan(e -> e.packages(
                                "org.hrodberaht.injection.testservices.largepackage"
                                , "org.hrodberaht.injection.testservices.annotated"
                        ))
                        .register(
                                e -> {
                                    e.register(AnyService.class).with(AnyServiceDoSomethingImpl.class);
                                    e.register(ATestingServiceInterface.class).with(ATestingService.class);
                                    e.register(BTestingServiceInnerInterface.class).with(BTestingServiceInner.class);
                                }
                        )
                        .getContainer();

        assertTheContainer(injectionContainer);

    }


    @Test

    public void verifyGoodErrorHandlingForServiceWithoutConstructor() throws Exception {
        InjectContainer injectionContainer =
                new InjectionRegistryBuilder()
                        .register(
                                e -> {
                                    e.register(AnyService.class).with(AnyServiceDoSomethingImpl.class);
                                }
                        )
                        .getContainer();

        try {
            injectionContainer.get(BaseServiceImplementation.class);
            assertEquals("do not", "reach this");
        } catch (Throwable error) {
            assertEquals(
                    "org.hrodberaht.injection.testservices.notregistered.BaseServiceImplementation " +
                            "is abstract and not registered in container, fix this by registering an implementation"
                    , error.getMessage());
        }
    }

    @Test
    public void multiModuleRegistrationLambda() throws Exception {
        Module moduleOne =
                new InjectionRegistryBuilder()
                        .scan(() -> "org.hrodberaht.injection.testservices.largepackage")
                        .register(
                                e -> {
                                    e.register(AnyService.class).with(AnyServiceDoSomethingImpl.class);
                                    e.register(ATestingServiceInterface.class).with(ATestingService.class);
                                    e.register(BTestingServiceInnerInterface.class).with(BTestingServiceInner.class);
                                }
                        )
                        .scan(() -> "org.hrodberaht.injection.testservices.annotated")
                        .getModule();

        Module moduleTwo =
                new InjectionRegistryBuilder()
                        .scan(() -> "org.hrodberaht.injection.testservices.interfaces")
                        .getModule();

        InjectContainer injectionContainer = new InjectionRegistryPlain()
                .register(moduleOne)
                .register(moduleTwo)
                .getContainer();

        assertTrue(
                TestingService.class
                        .equals(injectionContainer.get(TestingServiceInterface.class).getClass())
        );

        assertTheContainer(injectionContainer);

    }

    private void assertTheContainer(InjectContainer injectionContainer) {
        assertTrue(
                Volvo.class
                        .equals(injectionContainer.get(Car.class).getClass())
        );

        assertTrue(
                AnyServiceDoSomethingImpl.class
                        .equals(injectionContainer.get(AnyService.class).getClass())
        );

        assertTrue(
                ATestingService.class
                        .equals(injectionContainer.get(ATestingServiceInterface.class).getClass())
        );

        assertTrue(
                org.hrodberaht.injection.testservices.annotated.Volvo.class
                        .equals(injectionContainer.get(org.hrodberaht.injection.testservices.annotated.Car.class).getClass())
        );
    }
}

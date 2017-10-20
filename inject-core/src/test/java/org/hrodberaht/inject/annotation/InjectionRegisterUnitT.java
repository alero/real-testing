package org.hrodberaht.inject.annotation;

import org.hrodberaht.inject.testservices.interfaces.TestingService;
import org.hrodberaht.inject.testservices.interfaces.TestingServiceInterface;
import org.hrodberaht.inject.testservices.largepackage.Car;
import org.hrodberaht.inject.testservices.largepackage.Volvo;
import org.hrodberaht.inject.testservices.notregistered.BaseServiceImplementation;
import org.hrodberaht.inject.testservices.simple.AnyService;
import org.hrodberaht.inject.testservices.simple.AnyServiceDoSomethingImpl;
import org.hrodberaht.inject.testservices.sortedinterfaces.ATestingService;
import org.hrodberaht.inject.testservices.sortedinterfaces.ATestingServiceInterface;
import org.hrodberaht.inject.testservices.sortedinterfaces.BTestingServiceInner;
import org.hrodberaht.inject.testservices.sortedinterfaces.BTestingServiceInnerInterface;
import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.InjectionRegistryPlain;
import org.hrodberaht.injection.Module;
import org.hrodberaht.injection.stream.InjectionRegistryStream;
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
                this.scanAndRegister("org.hrodberaht.inject.testservices.largepackage");
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
                this.scanAndRegister("org.hrodberaht.inject.testservices.annotated");
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
                new InjectionRegistryStream()
                        .scan(() -> "org.hrodberaht.inject.testservices.largepackage")
                        .register(
                                e -> {
                                    e.register(AnyService.class).with(AnyServiceDoSomethingImpl.class);
                                    e.register(ATestingServiceInterface.class).with(ATestingService.class);
                                    e.register(BTestingServiceInnerInterface.class).with(BTestingServiceInner.class);
                                }
                        )
                        .scan(() -> "org.hrodberaht.inject.testservices.annotated")
                        .getContainer();

        assertTheContainer(injectionContainer);

    }

    @Test
    public void moduleMultiScanRegistrationLambda() throws Exception {
        InjectContainer injectionContainer =
                new InjectionRegistryStream()
                        .scan(e -> e.packages(
                                "org.hrodberaht.inject.testservices.largepackage"
                                , "org.hrodberaht.inject.testservices.annotated"
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
                new InjectionRegistryStream()
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
                    "org.hrodberaht.inject.testservices.notregistered.BaseServiceImplementation " +
                            "is abstract and not registered in container, fix this by registering an implementation"
                    , error.getMessage());
        }
    }

    @Test
    public void multiModuleRegistrationLambda() throws Exception {
        Module moduleOne =
                new InjectionRegistryStream()
                        .scan(() -> "org.hrodberaht.inject.testservices.largepackage")
                        .register(
                                e -> {
                                    e.register(AnyService.class).with(AnyServiceDoSomethingImpl.class);
                                    e.register(ATestingServiceInterface.class).with(ATestingService.class);
                                    e.register(BTestingServiceInnerInterface.class).with(BTestingServiceInner.class);
                                }
                        )
                        .scan(() -> "org.hrodberaht.inject.testservices.annotated")
                        .getModule();

        Module moduleTwo =
                new InjectionRegistryStream()
                        .scan(() -> "org.hrodberaht.inject.testservices.interfaces")
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
                org.hrodberaht.inject.testservices.annotated.Volvo.class
                        .equals(injectionContainer.get(org.hrodberaht.inject.testservices.annotated.Car.class).getClass())
        );
    }
}

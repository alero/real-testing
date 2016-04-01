package test.org.hrodberaht.inject.annotation;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.InjectionRegistryPlain;
import org.hrodberaht.injection.Module;
import org.hrodberaht.injection.stream.InjectionRegistryStream;
import org.junit.Test;
import test.org.hrodberaht.inject.testservices.interfaces.TestingService;
import test.org.hrodberaht.inject.testservices.interfaces.TestingServiceInterface;
import test.org.hrodberaht.inject.testservices.largepackage.Car;
import test.org.hrodberaht.inject.testservices.largepackage.Volvo;
import test.org.hrodberaht.inject.testservices.simple.AnyService;
import test.org.hrodberaht.inject.testservices.simple.AnyServiceDoSomethingImpl;
import test.org.hrodberaht.inject.testservices.sortedinterfaces.ATestingService;
import test.org.hrodberaht.inject.testservices.sortedinterfaces.ATestingServiceInterface;
import test.org.hrodberaht.inject.testservices.sortedinterfaces.BTestingServiceInner;
import test.org.hrodberaht.inject.testservices.sortedinterfaces.BTestingServiceInnerInterface;

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
                this.scanAndRegister("test.org.hrodberaht.inject.testservices.largepackage");
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
                this.scanAndRegister("test.org.hrodberaht.inject.testservices.annotated");
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
                    .scan(() -> "test.org.hrodberaht.inject.testservices.largepackage")
                    .register( e -> {
                                        e.create(AnyService.class).with(AnyServiceDoSomethingImpl.class);
                                        e.create(ATestingServiceInterface.class).with(ATestingService.class);
                                        e.create(BTestingServiceInnerInterface.class).with(BTestingServiceInner.class);
                                    }
                    )
                    .scan(() -> "test.org.hrodberaht.inject.testservices.annotated")
            .getContainer();

        assertTheContainer(injectionContainer);

    }

    @Test
    public void multiModuleRegistrationLambda() throws Exception {
        Module moduleOne =
                new InjectionRegistryStream()
                        .scan(() -> "test.org.hrodberaht.inject.testservices.largepackage")
                        .register( e -> {
                                    e.create(AnyService.class).with(AnyServiceDoSomethingImpl.class);
                                    e.create(ATestingServiceInterface.class).with(ATestingService.class);
                                    e.create(BTestingServiceInnerInterface.class).with(BTestingServiceInner.class);
                                }
                        )
                        .scan(() -> "test.org.hrodberaht.inject.testservices.annotated")
                    .getModule();

        Module moduleTwo =
                new InjectionRegistryStream()
                        .scan(() -> "test.org.hrodberaht.inject.testservices.interfaces")
                        .getModule();

        InjectContainer injectionContainer =new InjectionRegistryPlain()
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
                test.org.hrodberaht.inject.testservices.annotated.Volvo.class
                        .equals(injectionContainer.get(test.org.hrodberaht.inject.testservices.annotated.Car.class).getClass())
        );
    }
}

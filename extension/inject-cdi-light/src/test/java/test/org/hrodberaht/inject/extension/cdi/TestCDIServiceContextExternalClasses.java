package test.org.hrodberaht.inject.extension.cdi;

import org.hrodberaht.inject.extension.cdi.example.service.ExampleInterface;
import org.hrodberaht.inject.extension.tdd.ContainerContext;
import org.hrodberaht.inject.extension.tdd.JUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.org.hrodberaht.inject.extension.cdi.config.CDIContainerConfigExampleExternalResources;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:31:30
 * @version 1.0
 * @since 1.0
 */
@ContainerContext(CDIContainerConfigExampleExternalResources.class)
@RunWith(JUnitRunner.class)
public class TestCDIServiceContextExternalClasses {


    @Inject
    private ExampleInterface anInterface;

    @Test
    public void testWiring() {
        String something = anInterface.getSomething();
        assertEquals("something", something);

        String somethingDeep = anInterface.getSomethingElseLikeWhat();
        assertEquals("wait for it", somethingDeep);
    }


}

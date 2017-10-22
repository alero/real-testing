package org.hrodberaht.injection.plugin.junit;

import org.hrodberaht.injection.extensions.cdi.example.service.CDIExampleExtension;
import org.hrodberaht.injection.extensions.cdi.example.service.ExampleInterface;
import org.hrodberaht.injection.plugin.junit.cdi.config.CDIContainerConfigExampleExternalResources;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:31:30
 * @version 1.0
 * @since 1.0
 */
@ContainerContext(CDIContainerConfigExampleExternalResources.class)
@RunWith(JUnitRunner.class)
public class TestCDIServiceContextExternalClasses {


    @Inject
    private ExampleInterface anInterface;

    @Inject
    private CDIExampleExtension cdiExampleExtension;

    @Test
    public void testWiring() {
        String something = anInterface.getSomething();
        assertEquals("something", something);

        String somethingDeep = anInterface.getSomethingElseLikeWhat();
        assertEquals("wait for it", somethingDeep);

        assertTrue(cdiExampleExtension.isAfterBeanDiscoveryInitiated());
        assertTrue(cdiExampleExtension.isAfterBeanDiscoveryInitiated());
    }


}

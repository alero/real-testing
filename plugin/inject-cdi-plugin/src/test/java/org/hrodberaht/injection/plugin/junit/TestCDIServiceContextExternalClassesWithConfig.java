package org.hrodberaht.injection.plugin.junit;

import org.hrodberaht.injection.extensions.cdi.example.service.AnotherInterface;
import org.hrodberaht.injection.extensions.cdi.example.service.ExampleInterface;
import org.hrodberaht.injection.plugin.junit.cdi.config.CDIContainerConfigExampleExternalResourcesAndConfig;
import org.hrodberaht.injection.plugin.junit.cdi.service2.CDIServiceWithResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:31:30
 * @version 1.0
 * @since 1.0
 */
@ContainerContext(CDIContainerConfigExampleExternalResourcesAndConfig.class)
@RunWith(JUnitRunner.class)
public class TestCDIServiceContextExternalClassesWithConfig {


    @Inject
    private ExampleInterface anInterface;

    @Inject
    private AnotherInterface anotherInterface;

    @Inject
    private CDIServiceWithResource cdiServiceWithResource;

    @Test
    public void testWiring() {
        String something = anInterface.getSomething();
        assertEquals("something", something);

        String somethingDeep = anInterface.getSomethingElseLikeWhat();
        assertEquals("wait for it", somethingDeep);
    }

    @Test
    public void testResourceDefinitions() throws Exception {
        assertNotNull(anotherInterface.getDataSource());

        assertNotNull(anotherInterface.getEntityManager());
    }


    @Test
    public void testSQLInit() {

        String value = cdiServiceWithResource.findStuff(12L);
        assertEquals("The Name", value);

        value = cdiServiceWithResource.findStuff(11L);
        assertEquals("A Name", value);
    }

}

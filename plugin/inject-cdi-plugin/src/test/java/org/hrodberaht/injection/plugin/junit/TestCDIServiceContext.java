package org.hrodberaht.injection.plugin.junit;

import org.hrodberaht.injection.plugin.junit.cdi.config.CDIContainerConfigExample;
import org.hrodberaht.injection.plugin.junit.cdi.service.CDIServiceInterface;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:31:30
 * @version 1.0
 * @since 1.0
 */
@ContainerContext(CDIContainerConfigExample.class)
@RunWith(PluggableJUnitRunner.class)
public class TestCDIServiceContext {


    @Inject
    private CDIServiceInterface anInterface;

    @Test
    public void testWiring() {
        String something = anInterface.findSomething(12L);
        assertEquals("Something", something);

        String somethingDeep = anInterface.findSomethingDeep(12L);
        assertEquals("initialized", somethingDeep);
    }


}

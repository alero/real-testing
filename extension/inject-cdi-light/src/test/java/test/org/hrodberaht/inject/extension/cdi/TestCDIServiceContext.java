package test.org.hrodberaht.inject.extension.cdi;

import org.hrodberaht.injection.extensions.junit.ContainerContext;
import org.hrodberaht.injection.extensions.junit.JUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.org.hrodberaht.inject.extension.cdi.config.CDIContainerConfigExample;
import test.org.hrodberaht.inject.extension.cdi.service.CDIServiceInterface;

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
@ContainerContext(CDIContainerConfigExample.class)
@RunWith(JUnitRunner.class)
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

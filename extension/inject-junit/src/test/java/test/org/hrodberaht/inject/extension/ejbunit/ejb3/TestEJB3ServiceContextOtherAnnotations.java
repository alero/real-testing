package test.org.hrodberaht.inject.extension.ejbunit.ejb3;

import org.hrodberaht.inject.extension.tdd.ContainerContext;
import org.hrodberaht.inject.extension.tdd.JUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.org.hrodberaht.inject.extension.ejbunit.ejb3.config.EJBContainerConfigExample2;
import test.org.hrodberaht.inject.extension.ejbunit.ejb3.service2.EJB3ServiceInterface;
import test.org.hrodberaht.inject.extension.ejbunit.ejb3.service2.EJB3ServiceInterfaceRemote;

import javax.ejb.EJB;
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
@ContainerContext(EJBContainerConfigExample2.class)
@RunWith(JUnitRunner.class)
public class TestEJB3ServiceContextOtherAnnotations {


    @Inject
    private EJB3ServiceInterface ejb3ServiceInterfaceWithInject;

    @EJB
    private EJB3ServiceInterface anInterface;

    @EJB
    private EJB3ServiceInterfaceRemote anInterfaceRemote;

    @Test
    public void testEJBWiring() {
        String something = anInterface.findSomething(12L);
        assertEquals("Something", something);

        String somethingDeep = anInterface.findSomethingDeep(12L);
        assertEquals("Something Deep", somethingDeep);

    }

    @Test
    public void testEJBRemoteWiring() {
        String something = anInterfaceRemote.findSomething(12L);
        assertEquals("Something", something);

        String somethingDeep = anInterfaceRemote.findSomethingDeep(12L);
        assertEquals("Something Deep", somethingDeep);

    }

    @Test
    public void testEJBInjectWiring() {
        String something = ejb3ServiceInterfaceWithInject.findSomething(12L);
        assertEquals("Something", something);

        String somethingDeep = ejb3ServiceInterfaceWithInject.findSomethingDeep(12L);
        assertEquals("Something Deep", somethingDeep);
    }


}

package test.org.hrodberaht.inject.extension.cdi;

import org.hrodberaht.injection.extensions.tdd.ContainerContext;
import org.hrodberaht.injection.extensions.tdd.JUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.org.hrodberaht.inject.extension.cdi.config.CDIContainerConfigExampleForJSEResourceCreator;
import test.org.hrodberaht.inject.extension.cdi.service2.CDIServiceWithResource;

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
@ContainerContext(CDIContainerConfigExampleForJSEResourceCreator.class)
@RunWith(JUnitRunner.class)
public class TestCDIResourceCreatorForJSE {


    @Inject
    private CDIServiceWithResource anInterface;

    @Test
    public void testSQLInit() {

        String value = anInterface.findStuff(12L);
        assertEquals("The Name", value);

        value = anInterface.findStuff(11L);
        assertEquals("A Name", value);
    }

    @Test
    public void testWiring() {

        anInterface.createStuff(14L, "Hi");

        String value = anInterface.findStuff(14L);
        assertEquals("Hi", value);
    }

    @Test
    public void testWiring2() {

        anInterface.createStuff(13L, "Hi There");

        String value = anInterface.findStuff(13L);
        assertEquals("Hi There", value);
    }


}

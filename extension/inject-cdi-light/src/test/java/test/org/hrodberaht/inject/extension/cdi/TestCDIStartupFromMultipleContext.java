package test.org.hrodberaht.inject.extension.cdi;

import org.hrodberaht.inject.extension.cdi.example.service.CDIExampleExtension;
import org.hrodberaht.inject.extension.tdd.ContainerContext;
import org.hrodberaht.inject.extension.tdd.JUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.org.hrodberaht.inject.extension.cdi.cdi_ext.CDIExtension;
import test.org.hrodberaht.inject.extension.cdi.config.CDIContainerConfigExample;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit Test EJB (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:31:30
 * @version 1.0
 * @since 1.0
 */
@ContainerContext(CDIContainerConfigExample.class)
@RunWith(JUnitRunner.class)
public class TestCDIStartupFromMultipleContext {


    @Inject
    private CDIExampleExtension exampleExtension;

    @Inject
    private CDIExtension extension;

    @Test
    public void testWiring() {

        assertEquals(true, exampleExtension.isAfterBeanDiscoveryInitiated());

        assertEquals(true, exampleExtension.isBeforeBeanDiscoveryInitiated());

        assertNotNull(extension.constantClassLoadedPostContainer);

    }


}

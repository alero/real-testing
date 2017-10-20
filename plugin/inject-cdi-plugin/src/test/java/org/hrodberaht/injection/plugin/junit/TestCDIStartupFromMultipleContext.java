package org.hrodberaht.injection.plugin.junit;

import org.hrodberaht.injection.extensions.cdi.example.service.CDIExampleExtension;
import org.hrodberaht.injection.plugin.junit.cdi.cdi_ext.CDIExtension;
import org.hrodberaht.injection.plugin.junit.cdi.config.CDIContainerConfigExample;
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
@ContainerContext(CDIContainerConfigExample.class)
@RunWith(PluggableJUnitRunner.class)
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

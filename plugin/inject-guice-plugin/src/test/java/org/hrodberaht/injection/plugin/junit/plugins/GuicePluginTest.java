package org.hrodberaht.injection.plugin.junit.plugins;

import com.google.inject.Injector;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.PluggableJUnitRunner;
import org.hrodberaht.injection.plugin.junit.plugins.config.GuiceContainerConfig;
import org.hrodberaht.injection.plugin.junit.plugins.service.AService;
import org.hrodberaht.injection.plugin.junit.plugins.service.MoreServices;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@ContainerContext(GuiceContainerConfig.class)
@RunWith(PluggableJUnitRunner.class)
public class GuicePluginTest {

    @Inject
    private AService aService;

    @Inject
    private MoreServices moreServices;

    @Inject
    private Injector injector;

    @Test
    public void testForServiceWiring() throws Exception {
        assertEquals("inited", aService.doSomething());

        assertSame(moreServices, aService.getService());

        // TODO: figure out why its not the same ...
        assertSame(injector.getInstance(MoreServices.class), aService.getService());
    }
}
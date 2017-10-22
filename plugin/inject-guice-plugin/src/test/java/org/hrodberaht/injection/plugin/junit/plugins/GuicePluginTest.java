package org.hrodberaht.injection.plugin.junit.plugins;

import com.google.inject.Injector;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.JUnitRunner;
import org.hrodberaht.injection.plugin.junit.plugins.service.AService;
import org.hrodberaht.injection.plugin.junit.plugins.service.MoreServices;
import org.hrodberaht.injection.plugin.junit.plugins.service.config.GuiceModule;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@ContainerContext(GuicePluginTest.Config.class)
@RunWith(JUnitRunner.class)
public class GuicePluginTest {

    public static class Config extends ContainerContextConfigBase {
        @Override
        protected void register(InjectionRegistryBuilder registryBuilder) {
            activatePlugin(GuicePlugin.class).loadModules(new GuiceModule());
        }
    }

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
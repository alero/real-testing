/*
 * Copyright (c) 2017 org.hrodberaht
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hrodberaht.injection.plugin.junit.plugins;

import com.google.inject.Injector;
import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.JUnit4Runner;
import org.hrodberaht.injection.plugin.junit.plugins.test.service.AService;
import org.hrodberaht.injection.plugin.junit.plugins.test.service.MoreServices;
import org.hrodberaht.injection.plugin.junit.plugins.test.service.config.GuiceModule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@ContainerContext(GuicePluginTest.Config.class)
@RunWith(JUnit4Runner.class)
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

        assertSame(injector.getInstance(MoreServices.class), aService.getService());
    }

    public static class Config extends ContainerContextConfigBase {
        @Override
        public void register(InjectionRegistryBuilder registryBuilder) {
            activatePlugin(GuiceExtensionPlugin.class).guiceModules(new GuiceModule());
        }
    }
}
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

@ContainerContext(CDIContainerConfigExampleExternalResourcesAndConfig.class)
@RunWith(JUnit4Runner.class)
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

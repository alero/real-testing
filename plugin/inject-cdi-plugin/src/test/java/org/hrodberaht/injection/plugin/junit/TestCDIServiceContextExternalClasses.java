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

import org.hrodberaht.injection.extensions.cdi.example.service.CDIExampleExtension;
import org.hrodberaht.injection.extensions.cdi.example.service.ExampleInterface;
import org.hrodberaht.injection.plugin.junit.cdi.config.CDIContainerConfigExampleExternalResources;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContainerContext(CDIContainerConfigExampleExternalResources.class)
@RunWith(JUnit4Runner.class)
public class TestCDIServiceContextExternalClasses {


    @Inject
    private ExampleInterface anInterface;

    @Inject
    private CDIExampleExtension cdiExampleExtension;

    @Test
    public void testWiring() {
        String something = anInterface.getSomething();
        assertEquals("something", something);

        String somethingDeep = anInterface.getSomethingElseLikeWhat();
        assertEquals("wait for it", somethingDeep);

        assertTrue(cdiExampleExtension.isAfterBeanDiscoveryInitiated());
        assertTrue(cdiExampleExtension.isAfterBeanDiscoveryInitiated());
    }


}

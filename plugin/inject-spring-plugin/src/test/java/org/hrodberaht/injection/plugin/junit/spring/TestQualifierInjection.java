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

package org.hrodberaht.injection.plugin.junit.spring;

import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnitRunner;
import org.hrodberaht.injection.plugin.junit.spring.config.JUnitConfigExample;
import org.hrodberaht.injection.plugin.junit.spring.testservices.simple.ServiceForQualifier;
import org.hrodberaht.injection.plugin.junit.spring.testservices.spring.SpringBeanQualifierInterfaceImpl1;
import org.hrodberaht.injection.plugin.junit.spring.testservices.spring.SpringBeanQualifierInterfaceImpl2;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContainerContext(JUnitConfigExample.class)
@RunWith(JUnitRunner.class)
public class TestQualifierInjection {

    @Inject
    private ServiceForQualifier serviceForQualifier;

    @Test
    public void testServiceForQualifier() throws Exception {
        assertNotNull(serviceForQualifier.getSpringBeanInterface());
        assertTrue(serviceForQualifier.getSpringBeanInterface().getClass()
                .isAssignableFrom(SpringBeanQualifierInterfaceImpl1.class));

        assertNotNull(serviceForQualifier.getSpringBeanInterface2());
        assertTrue(serviceForQualifier.getSpringBeanInterface2().getClass()
                .isAssignableFrom(SpringBeanQualifierInterfaceImpl2.class));
    }
}

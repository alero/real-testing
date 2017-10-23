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
import org.hrodberaht.injection.plugin.junit.JUnit4Runner;
import org.hrodberaht.injection.plugin.junit.spring.beans.incubator.ContainerLifeCycleTestUtil;
import org.hrodberaht.injection.plugin.junit.spring.config.JUnitJavaConfigComboExample;
import org.hrodberaht.injection.plugin.junit.spring.testservices.simple.AnyServiceInner;
import org.hrodberaht.injection.plugin.junit.spring.testservices.spring.SpringBean;
import org.hrodberaht.injection.plugin.junit.spring.testservices.spring.SpringBeanInner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@ContainerContext(JUnitJavaConfigComboExample.class)
@RunWith(JUnit4Runner.class)
public class TestJavaConfigComboSimpleInjection {

    @Autowired
    private SpringBean springBean;

    @Inject
    private ContainerLifeCycleTestUtil containerLifeCycleTestUtil;

    @Autowired
    private SpringBeanInner springBeanInner;


    @Test
    public void testWiredResource() throws Exception {

        assertNotNull(springBean);

        assertNotNull(springBean.getNameFromDB());


    }

    @Test
    @Ignore
    public void testWiredReplacement() throws Exception {

        containerLifeCycleTestUtil.registerServiceInstance(SpringBean.class, new SpringBean() {
            @Override
            public AnyServiceInner getAnyServiceInner() {
                return null;
            }
        });


        containerLifeCycleTestUtil.reloadSpring();

        springBeanInner = containerLifeCycleTestUtil.getService(SpringBeanInner.class);

        assertNotNull(springBeanInner);

        assertNull(springBeanInner.getAnyServiceInner());

        assertNotNull(springBeanInner.getSpringBeanInner2());


    }


}

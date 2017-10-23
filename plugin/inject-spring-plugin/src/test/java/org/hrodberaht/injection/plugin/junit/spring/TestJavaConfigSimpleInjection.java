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
import org.hrodberaht.injection.plugin.junit.spring.beans.incubator.ContainerLifeCycleTestUtil;
import org.hrodberaht.injection.plugin.junit.spring.config.JUnitJavaConfigExample;
import org.hrodberaht.injection.plugin.junit.spring.testservices.simple.AnyServiceDoSomethingImpl;
import org.hrodberaht.injection.plugin.junit.spring.testservices.spring.SpringBean;
import org.hrodberaht.injection.plugin.junit.spring.testservices.spring.SpringBeanInner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContainerContext(JUnitJavaConfigExample.class)
@RunWith(JUnitRunner.class)
public class TestJavaConfigSimpleInjection {

    @Inject
    private AnyServiceDoSomethingImpl anyService;

    @Inject
    private ContainerLifeCycleTestUtil containerLifeCycleTestUtil;

    @Autowired
    private SpringBean springBean;

    @Autowired
    private SpringBeanInner springBeanInner;

    @Test
    public void testWired() throws Exception {

        assertNotNull(springBeanInner);

        assertNotNull(springBeanInner.getAnyServiceInner());

        assertNotNull(springBeanInner.getSpringBeanInner2());

        anyService.doStuff();
        Collection collection = anyService.getStuff();
        assertEquals(collection.size(), 1);
    }

    @Test
    public void testWiredResource() throws Exception {

        assertNotNull(springBean);

        assertNotNull(springBean.getNameFromDB());


    }


    @Test
    public void testDeepSpringWired() throws Exception {

        anyService.doStuff();
        Collection collection = anyService.getStuff();
        assertEquals(collection.size(), 1);

        assertNotNull(containerLifeCycleTestUtil.getService(AnyServiceDoSomethingImpl.class).getSpringBean());

        assertNotNull(containerLifeCycleTestUtil.getService(AnyServiceDoSomethingImpl.class).getSpringBean().getAnyServiceInner());

        assertNotNull(containerLifeCycleTestUtil.getService(AnyServiceDoSomethingImpl.class).getSpringBeanInner());

        assertNotNull(containerLifeCycleTestUtil.getService(AnyServiceDoSomethingImpl.class).getSpringBeanInner().getSpringBeanInner2());

        assertNotNull(containerLifeCycleTestUtil.getService(AnyServiceDoSomethingImpl.class).getSpringBeanInner().getAnyServiceInner());
    }

    @Test
    public void testSpringDataSource() throws Exception {

        anyService.doStuff();
        Collection collection = anyService.getStuff();
        assertEquals(collection.size(), 1);

        assertEquals("dude", anyService.getSpringBean().getNameFromDB());
        assertEquals("SpringBeanName", anyService.getSpringBean().getName());

    }
}

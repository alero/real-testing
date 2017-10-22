package org.hrodberaht.injection.plugin.junit.spring;


import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnitRunner;
import org.hrodberaht.injection.plugin.junit.spring.beans.incubator.ContainerLifeCycleTestUtil;
import org.hrodberaht.injection.plugin.junit.spring.config.SpringContainerConfigExample;
import org.hrodberaht.injection.plugin.junit.spring.testservices.simple.AnyServiceDoSomethingImpl;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by alexbrob on 2016-02-25.
 */
@ContainerContext(SpringContainerConfigExample.class)
@RunWith(JUnitRunner.class)
public class TestSimpleInjection {

    @Inject
    private AnyServiceDoSomethingImpl anyService;

    @Inject
    private ContainerLifeCycleTestUtil containerLifeCycleTestUtil;

    @Test
    public void testWired() throws Exception {
        anyService.doStuff();
        Collection collection = anyService.getStuff();
        assertEquals(collection.size(), 1);
    }

    @Test
    public void testSpringWired() throws Exception {

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

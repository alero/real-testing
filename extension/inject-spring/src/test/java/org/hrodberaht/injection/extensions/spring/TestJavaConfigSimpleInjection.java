package org.hrodberaht.injection.extensions.spring;


import org.hrodberaht.injection.extensions.junit.ContainerContext;
import org.hrodberaht.injection.extensions.junit.JUnitRunner;
import org.hrodberaht.injection.extensions.junit.util.ContainerLifeCycleTestUtil;
import org.hrodberaht.injection.extensions.spring.config.SpringContainerJavaConfigExample;
import org.hrodberaht.injection.extensions.spring.testservices.simple.AnyServiceDoSomethingImpl;
import org.hrodberaht.injection.extensions.spring.testservices.spring.SpringBean;
import org.hrodberaht.injection.extensions.spring.testservices.spring.SpringBeanInner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by alexbrob on 2016-02-25.
 */
@ContainerContext(SpringContainerJavaConfigExample.class)
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

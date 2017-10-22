package org.hrodberaht.injection.plugin.junit.spring;


import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnitRunner;
import org.hrodberaht.injection.plugin.junit.spring.beans.incubator.ContainerLifeCycleTestUtil;
import org.hrodberaht.injection.plugin.junit.spring.config.SpringContainerJavaConfigComboExample;
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

/**
 * Created by alexbrob on 2016-02-25.
 */
@ContainerContext(SpringContainerJavaConfigComboExample.class)
@RunWith(JUnitRunner.class)
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

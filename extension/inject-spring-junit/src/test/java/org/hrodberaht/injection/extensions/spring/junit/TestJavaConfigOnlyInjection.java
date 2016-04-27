package org.hrodberaht.injection.extensions.spring.junit;

import org.hrodberaht.injection.extensions.junit.ContainerContext;
import org.hrodberaht.injection.extensions.junit.JUnitRunner;
import org.hrodberaht.injection.extensions.junit.util.ContainerLifeCycleTestUtil;
import org.hrodberaht.injection.extensions.spring.junit.config.SpringJavaConfigExample;
import org.hrodberaht.injection.extensions.spring.junit.testservices.SpringBean;
import org.hrodberaht.injection.extensions.spring.junit.testservices.SpringBeanInner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by alexbrob on 2016-02-25.
 */
@ContainerContext(SpringJavaConfigExample.class)
@RunWith(JUnitRunner.class)
public class TestJavaConfigOnlyInjection {

    @Inject
    private ContainerLifeCycleTestUtil containerLifeCycleTestUtil;

    @Autowired
    private SpringBean springBean;

    @Autowired
    private SpringBeanInner springBeanInner;

    private String init;

    @PostConstruct
    public void init() {
        init = "initiated";
    }

    @Test
    public void testWired() throws Exception {


        assertNotNull(springBeanInner);

        assertNotNull(springBeanInner.getSpringBeanInner2());

        assertEquals("initiated", init);

    }

    @Test
    public void testWiredResource() throws Exception {

        assertNotNull(springBean);

        assertNotNull(springBean.getNameFromDB());


    }

}

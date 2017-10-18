package org.hrodberaht.injection.plugin.junit.spring;

import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.PluggableJUnitRunner;
import org.hrodberaht.injection.plugin.junit.spring.config.SpringJavaConfigExample2;
import org.hrodberaht.injection.plugin.junit.spring.testservices2.SpringBeanV2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.assertNotNull;

/**
 * Created by alexbrob on 2016-02-25.
 */
@ContainerContext(SpringJavaConfigExample2.class)
@RunWith(PluggableJUnitRunner.class)
public class TestJavaConfigOnlyInjectionQualifier {

    @Autowired
    @Qualifier("springBean")
    private SpringBeanV2 springBean;

    @Autowired
    @Qualifier("springBean2")
    private SpringBeanV2 springBean2;

    @Test
    public void testWiredResource() throws Exception {

        assertNotNull(springBean);

        assertNotNull(springBean.getNameFromDB());


    }

    @Test
    public void testWiredResource2() throws Exception {

        assertNotNull(springBean2);

        assertNotNull(springBean2.getNameFromDB());


    }

}

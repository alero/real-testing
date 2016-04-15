package org.hrodberaht.injection.extensions.spring;

import org.hrodberaht.injection.extensions.junit.ContainerContext;
import org.hrodberaht.injection.extensions.junit.JUnitRunner;
import org.hrodberaht.injection.extensions.spring.config.SpringJavaConfigExample2;
import org.hrodberaht.injection.extensions.spring.testservices2.SpringBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.assertNotNull;

/**
 * Created by alexbrob on 2016-02-25.
 */
@ContainerContext(SpringJavaConfigExample2.class)
@RunWith(JUnitRunner.class)
public class TestJavaConfigOnlyInjectionQualifier {

    @Autowired
    @Qualifier("springBean")
    private SpringBean springBean;

    @Autowired
    @Qualifier("springBean2")
    private SpringBean springBean2;


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

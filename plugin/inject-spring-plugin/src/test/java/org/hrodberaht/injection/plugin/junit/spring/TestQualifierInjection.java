package org.hrodberaht.injection.plugin.junit.spring;

import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnitRunner;
import org.hrodberaht.injection.plugin.junit.spring.config.SpringContainerConfigExample;
import org.hrodberaht.injection.plugin.junit.spring.testservices.simple.ServiceForQualifier;
import org.hrodberaht.injection.plugin.junit.spring.testservices.spring.SpringBeanQualifierInterfaceImpl1;
import org.hrodberaht.injection.plugin.junit.spring.testservices.spring.SpringBeanQualifierInterfaceImpl2;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by alexbrob on 2016-04-01.
 */
@ContainerContext(SpringContainerConfigExample.class)
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

package org.hrodberaht.injection.extensions.spring;

import org.hrodberaht.injection.extensions.junit.ContainerContext;
import org.hrodberaht.injection.extensions.junit.JUnitRunner;
import org.hrodberaht.injection.extensions.spring.config.SpringContainerConfigExample;
import org.hrodberaht.injection.extensions.spring.testservices.simple.ServiceForQualifier;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

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
        assertNotNull(serviceForQualifier.getSpringBeanInterface2());
    }
}
package org.hrodberaht.injection.plugin.junit.spring;

import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.PluggableJUnitRunner;
import org.hrodberaht.injection.plugin.junit.spring.config.SpringContainerConfigExample;
import org.hrodberaht.injection.plugin.junit.spring.testservices.simple.ServiceForInterface;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

/**
 * Created by alexbrob on 2016-04-01.
 */
@ContainerContext(SpringContainerConfigExample.class)
@RunWith(PluggableJUnitRunner.class)
public class TestInterfaceInjection {

    @Inject
    private ServiceForInterface serviceForInterface;

    @Test
    public void testServiceForQualifier() throws Exception {
        assertNotNull(serviceForInterface.getSpringBeanInterface());
    }
}

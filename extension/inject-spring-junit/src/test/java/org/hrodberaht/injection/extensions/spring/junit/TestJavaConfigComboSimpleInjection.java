package org.hrodberaht.injection.extensions.spring.junit;


import org.hrodberaht.injection.extensions.junit.ContainerContext;
import org.hrodberaht.injection.extensions.junit.JUnitRunner;
import org.hrodberaht.injection.extensions.spring.junit.config.SpringContainerJavaConfigComboExampleStream;
import org.hrodberaht.injection.extensions.spring.junit.testservices.SpringBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

/**
 * Created by alexbrob on 2016-02-25.
 */
@ContainerContext(SpringContainerJavaConfigComboExampleStream.class)
@RunWith(JUnitRunner.class)
public class TestJavaConfigComboSimpleInjection {

    @Autowired
    private SpringBean springBean;


    @Test
    public void testWiredResource() throws Exception {

        assertNotNull(springBean);

        assertNotNull(springBean.getNameFromDB());


    }


}

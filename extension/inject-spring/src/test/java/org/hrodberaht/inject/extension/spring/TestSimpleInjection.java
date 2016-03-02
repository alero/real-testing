package org.hrodberaht.inject.extension.spring;

import org.hrodberaht.inject.extension.spring.config.SpringContainerConfigExample;
import org.hrodberaht.inject.extension.tdd.ContainerContext;
import org.hrodberaht.inject.extension.tdd.JUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by alexbrob on 2016-02-25.
 */
@ContainerContext(SpringContainerConfigExample.class)
@RunWith(JUnitRunner.class)
public class TestSimpleInjection {


    @Test
    public void testName() throws Exception {
        assertEquals("","");
    }
}

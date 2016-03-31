package org.hrodberaht.injection.extensions.spring;


import org.hrodberaht.injection.extensions.junit.ContainerContext;
import org.hrodberaht.injection.extensions.junit.JUnitRunner;
import org.hrodberaht.injection.extensions.spring.config.SpringContainerConfigExample;
import org.hrodberaht.injection.extensions.spring.testservices.simple.AnyService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Collection;

/**
 * Created by alexbrob on 2016-02-25.
 */
@ContainerContext(SpringContainerConfigExample.class)
@RunWith(JUnitRunner.class)
public class TestSimpleInjection {

    @Inject
    private AnyService anyService;

    @Test
    public void testWired() throws Exception {
        anyService.doStuff();
        Collection collection = anyService.getStuff();
        Assert.assertEquals(collection.size(), 1);
    }
}

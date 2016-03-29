package org.hrodberaht.injection.extensions.spring;


import org.hrodberaht.injection.extensions.spring.config.SpringContainerConfigExample;
import org.hrodberaht.injection.extensions.spring.testservices.simple.AnyService;
import org.hrodberaht.injection.extensions.tdd.ContainerContext;
import org.hrodberaht.injection.extensions.tdd.JUnitRunner;
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
        Collection collection = anyService.getStuff();
        Assert.assertEquals(collection.size(), 1);
    }
}

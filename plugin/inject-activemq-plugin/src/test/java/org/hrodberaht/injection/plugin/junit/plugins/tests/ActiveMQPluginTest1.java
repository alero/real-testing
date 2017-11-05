package org.hrodberaht.injection.plugin.junit.plugins.tests;

import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnit4Runner;
import org.hrodberaht.injection.plugin.junit.plugins.tests.service.MyConsumer;
import org.hrodberaht.injection.plugin.junit.plugins.tests.service.MyProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@ContainerContext(TestContextConfig.class)
@RunWith(JUnit4Runner.class)
public class ActiveMQPluginTest1 {

    @Autowired
    private MyProducer producerSimple;

    @Autowired
    private MyConsumer consumerSimple;

    @Test
    public void testForProducerConsumer() throws Exception {

        producerSimple.send("testqueue", "hello");
        assertEquals(1, consumerSimple.getLatch().getCount());
        consumerSimple.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertEquals(0, consumerSimple.getLatch().getCount());

    }


}
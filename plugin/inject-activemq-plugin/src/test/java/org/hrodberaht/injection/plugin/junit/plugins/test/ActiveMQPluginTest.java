package org.hrodberaht.injection.plugin.junit.plugins.test;

import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.JUnit4Runner;
import org.hrodberaht.injection.plugin.junit.plugins.ActiveMQPlugin;
import org.hrodberaht.injection.plugin.junit.plugins.SpringExtensionPlugin;
import org.hrodberaht.injection.plugin.junit.plugins.test.service.ConsumerSimple;
import org.hrodberaht.injection.plugin.junit.plugins.test.service.ProducerSimple;
import org.hrodberaht.injection.plugin.junit.plugins.test.spring.ConsumerSimpleConfig;
import org.hrodberaht.injection.plugin.junit.plugins.test.spring.ProducerSimpleConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@ContainerContext(ActiveMQPluginTest.InnerConfig.class)
@RunWith(JUnit4Runner.class)
public class ActiveMQPluginTest {

    @Autowired
    private ProducerSimple producerSimple;

    @Autowired
    private ConsumerSimple consumerSimple;

    @Test
    public void testForProducerConsumer() throws Exception {

        producerSimple.send("testqueue", "hello");
        assertEquals(1, consumerSimple.getLatch().getCount());

        consumerSimple.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertEquals(0, consumerSimple.getLatch().getCount());

    }

    @Test
    public void testForProducerConsumer2() throws Exception {

        producerSimple.send("testqueue", "hello");
        assertEquals(1, consumerSimple.getLatch().getCount());

        consumerSimple.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertEquals(0, consumerSimple.getLatch().getCount());

    }


    public static class InnerConfig extends ContainerContextConfigBase {
        @Override
        public void register(InjectionRegistryBuilder registryBuilder) {
            ActiveMQPlugin activatePlugin = activatePlugin(ActiveMQPlugin.class);

            activatePlugin(SpringExtensionPlugin.class)
                    .with(activatePlugin)
                    .springConfig(ProducerSimpleConfig.class, ConsumerSimpleConfig.class)
            ;
        }
    }
}
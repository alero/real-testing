package org.hrodberaht.injection.plugin.junit.plugins;

import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.JUnit5Extension;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.utils.KafkaConsumerProducerSample;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@ContainerContext(TestJUnitKafkaConsumerProducer.InnerConfig.class)
@ExtendWith(JUnit5Extension.class)
public class TestJUnitKafkaConsumerProducer {

    private static final Logger LOG = LoggerFactory.getLogger(TestJUnitKafkaConsumerProducer.class);

    @Inject
    private KafkaPlugin kafkaPlugin;


    @Test
    public void testForKafkaSetup(){
        LOG.info("before test -  testForKafkaSetup");
        KafkaConsumerProducerSample.testBasicConsumerProducer(kafkaPlugin.getEmbedded());
        LOG.info("after test -  testForKafkaSetup");
    }

    @Test
    public void testForKafkaSetup2(){
        LOG.info("before test -  testForKafkaSetup2");
        KafkaConsumerProducerSample.testBasicConsumerProducer(kafkaPlugin.getEmbedded());
        LOG.info("after test -  testForKafkaSetup2");
    }

    public static class InnerConfig extends ContainerContextConfigBase {
        @Override
        public void register(InjectionRegistryBuilder registryBuilder) {
            activatePlugin(new KafkaPlugin(Plugin.LifeCycle.TEST_CONFIG));
        }
    }
}

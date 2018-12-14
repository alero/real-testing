package org.hrodberaht.injection.plugin.junit.plugins;

import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnit5Extension;
import org.hrodberaht.injection.plugin.junit.utils.KafkaConsumerProducerSample;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@ContainerContext(TestJUnitKafkaConsumerProducerConfigLifecycle.InnerConfig.class)
@ExtendWith(JUnit5Extension.class)
public class TestJUnitKafkaConsumerProducerConfigLifecycle2 {

    private static final Logger LOG = LoggerFactory.getLogger(TestJUnitKafkaConsumerProducerConfigLifecycle2.class);

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


}

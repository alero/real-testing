package org.hrodberaht.injection.plugin.junit.service.sample;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class OffsetBeginningRebalanceListener implements ConsumerRebalanceListener {

    public static Logger logger = LoggerFactory.getLogger(OffsetBeginningRebalanceListener.class);

    private final KafkaListenerRunner.KafkaConsumerWrapper<String, String> consumer;
    private boolean resetDone = false;

    public OffsetBeginningRebalanceListener(KafkaListenerRunner.KafkaConsumerWrapper<String, String> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {

        StringBuilder logData = new StringBuilder("Revoked from");

        for (TopicPartition partition : partitions) {
            logData.append("collection = [" + partition + "]");
        }

        logger.info(logData.toString());
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

        StringBuilder logData = new StringBuilder(consumer.getClientId()+" - Assigned to: ");

        for (TopicPartition partition : partitions) {
            logData.append(partition + " ");
        }

        logger.info(logData.toString());

        if (!resetDone) {
            logger.info("Seek to beginning of partitions for consumer {}", consumer.getClientId());
            consumer.getConsumer().seekToBeginning(partitions);
            resetDone = true;
        }
    }

}

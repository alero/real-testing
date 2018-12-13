package org.hrodberaht.injection.plugin.junit.service.sample;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;

public class OffsetBeginningRebalanceListener implements ConsumerRebalanceListener {

    public static Logger logger = LoggerFactory.getLogger(OffsetBeginningRebalanceListener.class);

    private final KafkaConsumer<String, String> consumer;
    private boolean resetDone = false;
    private String partition;

    public OffsetBeginningRebalanceListener(KafkaConsumer<String, String> consumer, String partition) {
        this.consumer = consumer;
        this.partition = partition;
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

        StringBuilder logData = new StringBuilder("Assigned to: ");

        for (TopicPartition partition : partitions) {
            logData.append(partition + " ");
        }

        logger.info(logData.toString());

        if (!resetDone) {
            consumer.seekToBeginning(Collections.singletonList(new TopicPartition(partition,0)));
            resetDone = true;
        }
    }

}

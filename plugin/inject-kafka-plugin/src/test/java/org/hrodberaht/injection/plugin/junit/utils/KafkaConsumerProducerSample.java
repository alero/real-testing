package org.hrodberaht.injection.plugin.junit.utils;

import com.salesforce.kafka.test.KafkaTestServer;
import com.salesforce.kafka.test.KafkaTestUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerProducerSample {


    public static void testBasicConsumerProducer(KafkaTestServer kafkaTestServer){

        final String theTopic = "transactional-topic" + System.currentTimeMillis();


        final KafkaTestUtils kafkaTestUtils = new KafkaTestUtils(kafkaTestServer);

        // Create a topic.
        // kafkaTestUtils.createTopic(theTopic, 1, (short) 1);

        // Define override properties.
        Properties config = new Properties();
        config.put("group.id", "test-consumer-group");
        config.put("enable.auto.commit", "false");
        config.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        config.put("auto.offset.reset", "earliest");


        try (final KafkaConsumer<String, String> consumer
                     = kafkaTestUtils.getKafkaConsumer(StringDeserializer.class, StringDeserializer.class, config)) {

            // Subscribe to the topic
            consumer.subscribe(Collections.singletonList(theTopic));

            // Setup the producer
            config = new Properties();
            config.put("transactional.id", "MyRandomString" + System.currentTimeMillis());

            try (final KafkaProducer<String, String> producer
                         = kafkaTestUtils.getKafkaProducer(StringSerializer.class, StringSerializer.class, config)) {
                // Init transaction and begin
                producer.initTransactions();
                producer.beginTransaction();

                // Define our test message and key
                final String theKey = "Here is the Key";
                final String theMsg = "Here is the message";
                final ProducerRecord<String, String> r = new ProducerRecord<>(theTopic, theKey, theMsg);

                // Send and commit the record.
                producer.send(r);
                producer.commitTransaction();

                // Use consumer to read the message
                final ConsumerRecords<String, String> records = consumer.poll(Duration.of(5, ChronoUnit.SECONDS));
                Assertions.assertFalse(records.isEmpty(), "Should not be empty!");
                Assertions.assertEquals(1, records.count(), "Should have a single record");
                for (final ConsumerRecord<String, String> record : records) {
                    Assertions.assertEquals(theKey, record.key(), "Keys should match");
                    Assertions.assertEquals(theMsg, record.value(), "Values should match");
                    consumer.commitSync();
                }

            }
        }
    }

}

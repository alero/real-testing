package org.hrodberaht.injection.plugin.junit.service.sample;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.salesforce.kafka.test.KafkaProvider;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import org.hrodberaht.injection.plugin.junit.plugins.KafkaAdminUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG;

/**
 * Über Spring-Kafka kann Stand 1.2 kein RebalanceListener eingeklinkt werden!
 * Daher ist diese Handarbeit noch notwendig! Kann sicher ändern!
 **/

@Singleton
public class KafkaListenerRunner implements Runnable {

    private static Logger LOG = LoggerFactory.getLogger(KafkaListenerRunner.class);

    @Inject
    private ArticlesStore store;

    @Inject
    private KafkaProvider kafkaProvider;

    @Inject
    private KafkaAdminUtil adminUtil;

    private final Gson gson = new Gson();
    private final JsonParser parser = new JsonParser();


    private Map<String, Object> config(boolean simpleSeek, String name) {

        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaProvider.getKafkaConnectString());
        props.put(GROUP_ID_CONFIG, name);
        props.put(CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        props.put(ENABLE_AUTO_COMMIT_CONFIG, "true");

        if(!simpleSeek){
            props.put(MAX_POLL_RECORDS_CONFIG, 50);
            props.put("auto.offset.reset", "earliest");
        }
        props.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        //props.put(SESSION_TIMEOUT_MS_CONFIG, "10000");
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        return props;
    }

    public void run() {
        consumeForever("article_store", "articles");
    }

    public void runOnce(String groupName, String topicName) {
        try (KafkaConsumerWrapper<String, String> consumer = createConsumer(true, groupName, topicName)) {
            consumeOnce(consumer);
        }
    }

    private void consumeForever(String groupName, String topicName) {
        try (KafkaConsumerWrapper<String, String> consumer = createConsumer(true, groupName, topicName)){
            while (true) {
                consumeOnce(consumer);
            }
        } catch (Exception e) {
            LOG.error("bad code", e);
        }
    }

    public KafkaConsumerWrapper<String, String> getConsumer(String groupName, String topicName) {
        return createConsumer(false, groupName, topicName);
    }

    private KafkaConsumerWrapper<String, String> createConsumer(boolean simpleSeek, String groupName, String topicName) {
        Map<String, Object> configMap = config(simpleSeek, groupName);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configMap);
        KafkaConsumerWrapper<String, String> kafkaConsumerWrapper = new KafkaConsumerWrapper<>(configMap, consumer);
        if(simpleSeek){
            seekConsumerFromBeginning(consumer, topicName);
        }else {
            consumer.subscribe(Arrays.asList(topicName),
                    new OffsetBeginningRebalanceListener(kafkaConsumerWrapper));
        }

        return kafkaConsumerWrapper;
    }

    private void seekConsumerFromBeginning(KafkaConsumer<String, String> consumer, String topicName) {
        TopicDescription topicDescription = adminUtil.describeTopic(topicName);
        Collection<Integer> partitionIds = topicDescription.partitions().stream().map(TopicPartitionInfo::partition).collect(Collectors.toList());


        // Create topic Partitions
        final List<TopicPartition> topicPartitions = partitionIds
                .stream()
                .map((partitionId) -> new TopicPartition(topicName, partitionId))
                .collect(Collectors.toList());


        consumer.assign(topicPartitions);
        consumer.seekToBeginning(topicPartitions);
    }

    /**
     *
     * @param consumer
     * @return -1 = consumed nothing, > 0 consume count
     */
    public int consumeOnce(KafkaConsumerWrapper<String, String> consumer) {
        LOG.info("Starting poll");
        ConsumerRecords<String, String> records = consumer.consumer.poll(Duration.of(1, ChronoUnit.SECONDS));

        if (records.isEmpty()) {
            LOG.info("Found nothing on the topic");
            return -1;
        }

        if (!records.isEmpty()) {
            LOG.info("Found {} records on the topic", records.count());
        }


        for (ConsumerRecord<String, String> cr : records) {


            JsonObject json = parser.parse(cr.value()).getAsJsonObject();

            String action = json.getAsJsonPrimitive("action").getAsString();

            JsonObject object = json.getAsJsonObject("object");

            //LOG.info("----------------------------------------------------------------------------------");
            //LOG.info("Offset: " + cr.offset());
            //LOG.info("ConsumerId: "+consumer.getClientId());
            //LOG.info("Key: "+ cr.key());
            //LOG.info("Action: " + action);
            //LOG.info("Object: " + object);

            Article article = gson.fromJson(object, Article.class);

            switch (action) {
                case "update":
                case "create":
                    article.setId(cr.key());
                    store.save(article);
                    break;
                case "delete":
                    store.delete(cr.key());
                    break;

            }


        }
        return records.count();
    }


    public static class KafkaConsumerWrapper<K,V> implements AutoCloseable{
        private final String clientId;
        private final String groupId;
        private final KafkaConsumer<K,V> consumer;


        public KafkaConsumerWrapper(Map<String, Object> objectMap, KafkaConsumer<K, V> consumer) {
            this.clientId = objectMap.get(CLIENT_ID_CONFIG).toString();
            this.groupId = objectMap.get(GROUP_ID_CONFIG).toString();
            this.consumer = consumer;
        }

        public String getClientId() {
            return clientId;
        }

        public String getGroupId() {
            return groupId;
        }

        public KafkaConsumer<K, V> getConsumer() {
            return consumer;
        }

        @Override
        public void close() {
            consumer.close();
        }
    }

}




package org.hrodberaht.injection.plugin.junit.service.sample;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.salesforce.kafka.test.KafkaProvider;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;

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

    private final Gson gson = new Gson();
    private final JsonParser parser = new JsonParser();

    private Map<String, Object> config(String name) {

        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaProvider.getKafkaConnectString());
        props.put(GROUP_ID_CONFIG, name);
        props.put(ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(SESSION_TIMEOUT_MS_CONFIG, "10000");
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        return props;
    }

    public void run() {
        consumeForever("article_store");
    }

    public void runOnce(String name) {
        try (KafkaConsumer<String, String> consumer = createConsumer(true, name)) {
            consumeOnce(consumer);
        }
    }

    public void runOnceWithSubscribe(String name) {
        try (KafkaConsumer<String, String> consumer = createConsumer(false, name)) {
            consumeOnce(consumer);
        }
    }

    private void consumeForever(String name) {
        try (KafkaConsumer<String, String> consumer = createConsumer(true, name)){
            while (true) {
                if (!consumeOnce(consumer)) continue;
            }
        } catch (Exception e) {
            LOG.error("bad code", e);
        }
    }

    public KafkaConsumer<String, String> getConsumer(String name) {
        return createConsumer(false, name);
    }

    private KafkaConsumer<String, String> createConsumer(boolean simpleSeek, String name) {

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(config(name));

        if(simpleSeek){
            seekConsumerFromBeginning(consumer);
        }else {
            consumer.subscribe(Arrays.asList(ArticlesService.TOPIC_NAME));
        }

        return consumer;
    }

    private void seekConsumerFromBeginning(KafkaConsumer<String, String> consumer) {
        TopicDescription topicDescription = this.describeTopic(ArticlesService.TOPIC_NAME);
        Collection<Integer> partitionIds = topicDescription.partitions().stream().map(TopicPartitionInfo::partition).collect(Collectors.toList());


        // Create topic Partitions
        final List<TopicPartition> topicPartitions = partitionIds
                .stream()
                .map((partitionId) -> new TopicPartition(ArticlesService.TOPIC_NAME, partitionId))
                .collect(Collectors.toList());


        consumer.assign(topicPartitions);
        consumer.seekToBeginning(topicPartitions);
    }

    /**
     *
     * @param consumer
     * @return true = consumed something
     */
    public boolean consumeOnce(KafkaConsumer<String, String> consumer) {
        LOG.info("Starting poll");
        ConsumerRecords<String, String> records = consumer.poll(Duration.of(10, ChronoUnit.SECONDS));

        if (records.isEmpty()) {
            LOG.info("Found nothing on the topic");
            return false;
        }


        for (ConsumerRecord<String, String> cr : records) {


            JsonObject json = parser.parse(cr.value()).getAsJsonObject();

            String action = json.getAsJsonPrimitive("action").getAsString();

            JsonObject object = json.getAsJsonObject("object");

            LOG.info("----------------------------------------------------------------------------------");
            LOG.info("Offset: " + cr.offset());
            LOG.info("Key: "+ cr.key());
            LOG.info("Action: " + action);
            LOG.info("Object: " + object);

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
        return true;
    }

    public TopicDescription describeTopic(final String topicName) {
        // Create admin client
        try (final AdminClient adminClient = getAdminClient()) {
            // Make async call to describe the topic.
            final DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Collections.singleton(topicName));

            return describeTopicsResult.values().get(topicName).get();
        } catch (final InterruptedException | ExecutionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private AdminClient getAdminClient() {
        return KafkaAdminClient.create(buildDefaultClientConfig());
    }

    private Map<String, Object> buildDefaultClientConfig() {
        final Map<String, Object> defaultClientConfig = new HashMap<>();
        defaultClientConfig.put("bootstrap.servers", kafkaProvider.getKafkaConnectString());
        defaultClientConfig.put("client.id", "admin-consumer-1");
        return defaultClientConfig;
    }
}




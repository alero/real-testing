package org.hrodberaht.injection.plugin.junit.service.sample;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.salesforce.kafka.test.KafkaProvider;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BUFFER_MEMORY_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

@Singleton
public class ArticlesService {

    private static Logger LOG = LoggerFactory.getLogger(ArticlesService.class);

    @Inject
    private KafkaProvider kafkaProvider;

    @Inject
    private ArticlesStore store;

    private String topicName;

    private final Gson gson = new Gson();

    private Producer<String, String> producer;

    @PostConstruct
    public void init(){
        producer = producer(kafkaProvider);
    }

    public void topicName(String topicName){
        this.topicName = topicName;
    }

    public Collection<Article> getArticles() {
        return store.getAll();
    }


    public Article getArticle(String id) {

        Article a = store.get(id);

        if (a == null) {
            throw new IllegalArgumentException();
        }

        return a;
    }


    public String save(Article article) {

        try {
            String id = UUID.randomUUID().toString();
            article.setId(id);
            RecordMetadata recordMetadata = producer.send(new ProducerRecord<>(topicName, id, createWrapper( article))).get();
            LOG.info("Sent message to topic {}, offset:{}, partition:{}, size:{}", topicName, recordMetadata.offset(), recordMetadata.partition(), recordMetadata.serializedValueSize());
            return id;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    public void delete(String id) {
        producer.send(new ProducerRecord<>(topicName, id, deleteWrapper(id)));
    }

    public void update(String id, Article article) {
        producer.send(new ProducerRecord<>(topicName, id, putWrapper(article)));
    }

    public void flush(){
        producer.flush();
    }

    public long getCount() {
        return store.getSize();
    }

    private String createWrapper( Article article) {
        JsonObject cmd = new JsonObject();
        cmd.addProperty("action", "create");
        cmd.add("object", gson.toJsonTree(article));
        return cmd.toString();
    }

    private String deleteWrapper( String id) {
        JsonObject cmd = new JsonObject();
        cmd.addProperty("action", "delete");
        return cmd.toString();
    }

    private String putWrapper( Article article) {
        JsonObject cmd = new JsonObject();
        cmd.addProperty("action", "update");
        cmd.add("object", gson.toJsonTree(article));
        return cmd.toString();
    }


    private Producer<String, String> producer(KafkaProvider kafkaProvider) {
        Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaProvider.getKafkaConnectString());
        props.put(ACKS_CONFIG, "all");
        props.put(RETRIES_CONFIG, 1);
        props.put(BATCH_SIZE_CONFIG, 10);
        // props.put(LINGER_MS_CONFIG, 100);
        props.put(BUFFER_MEMORY_CONFIG, 33554432);
        props.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<>(props);
    }

    public void clean() {
        store.clean();
    }
}

package org.hrodberaht.injection.plugin.junit.plugins;

import com.salesforce.kafka.test.KafkaProvider;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.TopicDescription;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class KafkaAdminUtil {

    private final KafkaProvider kafkaProvider;

    @Inject
    public KafkaAdminUtil(KafkaProvider kafkaProvider) {
        this.kafkaProvider = kafkaProvider;
    }

    public TopicDescription describeTopic(final String topicName) {
        // Create admin client
        try (final AdminClient adminClient = getAdminClient()) {
            // Make async call to describe the topic.
            final DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Collections.singleton(topicName));

            return describeTopicsResult.values().get(topicName).get();
        } catch (final InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
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

    public void deleteAllData() {
        /*
        try (final AdminClient adminClient = getAdminClient()) {
            ListTopicsResult listTopicsResult = adminClient.listTopics();
            try {
                for(TopicListing topicListing : listTopicsResult.listings().get() ){
                    adminClient.listtopicListing.name()
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        */
    }
}

package org.hrodberaht.injection.plugin.junit.plugins;

import com.salesforce.kafka.test.KafkaTestCluster;

public class KafkaEmbeddedCluster {

    private final KafkaTestCluster kafkaTestCluster;
    private final String kafkaStorageDir;

    public KafkaEmbeddedCluster(KafkaTestCluster kafkaTestCluster, String kafkaStorageDir) {
        this.kafkaTestCluster = kafkaTestCluster;
        this.kafkaStorageDir = kafkaStorageDir;
    }

    public KafkaTestCluster getKafkaTestCluster() {
        return kafkaTestCluster;
    }

    String getKafkaStorageDir() {
        return kafkaStorageDir;
    }
}

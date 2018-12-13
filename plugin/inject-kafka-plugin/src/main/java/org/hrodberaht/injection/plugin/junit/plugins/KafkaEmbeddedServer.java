package org.hrodberaht.injection.plugin.junit.plugins;

import com.salesforce.kafka.test.KafkaTestServer;

public class KafkaEmbeddedServer {

    private final KafkaTestServer kafkaTestServer;
    private final String kafkaStorageDir;

    public KafkaEmbeddedServer(KafkaTestServer kafkaTestServer, String kafkaStorageDir) {
        this.kafkaTestServer = kafkaTestServer;
        this.kafkaStorageDir = kafkaStorageDir;
    }

    public KafkaTestServer getKafkaTestServer() {
        return kafkaTestServer;
    }

    String getKafkaStorageDir() {
        return kafkaStorageDir;
    }
}

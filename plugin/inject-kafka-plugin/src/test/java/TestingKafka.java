import com.salesforce.kafka.test.KafkaTestServer;
import org.hrodberaht.injection.plugin.junit.utils.KafkaConsumerProducerSample;
import org.junit.jupiter.api.Test;

import java.util.Properties;

public class TestingKafka {


    @Test
    void testExactlyOnceTransaction() throws Exception {
        // Define topic to test with.

        // Create our test server instance.

        Properties serverConfig = new Properties();
        serverConfig.put("log.dir", "target/kafka_testing/manual");

        try (final KafkaTestServer kafkaTestServer = new KafkaTestServer(serverConfig)) {
            // Start it and create our topic.
            kafkaTestServer.start();

            // Create test utils instance.
            KafkaConsumerProducerSample.testBasicConsumerProducer(kafkaTestServer);
        }

    }
}

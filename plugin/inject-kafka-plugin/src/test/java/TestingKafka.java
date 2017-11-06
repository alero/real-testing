import org.junit.ClassRule;
import org.springframework.kafka.test.rule.KafkaEmbedded;

public class TestingKafka {


    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, "topic");

}

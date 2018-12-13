package org.hrodberaht.injection.plugin.junit.service.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class KafkaListenerManager {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerManager.class);

    private ExecutorService executor;
    private boolean stop = false;
    private final AtomicLong consumeCounter = new AtomicLong(0);

    public KafkaListenerManager(KafkaListenerRunner kafkaListenerRunner, String groupName, String topicName, int consumers) {
        executor = Executors.newFixedThreadPool(consumers);
        for (int i = 0; i < consumers; i++) {
            executor.execute(() -> {
                        KafkaListenerRunner.KafkaConsumerWrapper<String, String> consumer = kafkaListenerRunner.getConsumer(groupName, topicName);
                        while (!stop) {
                            int counter = kafkaListenerRunner.consumeOnce(consumer);
                            if (counter > 0) {
                                consumeCounter.getAndAdd(counter);
                            }
                        }
                        consumer.close();
                    }
            );
        }

    }

    public void stop() {
        stop = true;
        executor.shutdown();
    }

    public void waitForConsume(int counter) {
        waitForConsume(counter, 10000);
    }

    public void waitForConsume(int counter, int maxWaitMillis) {
        int i = 0;
        while(consumeCounter.get() < counter){
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
            }
            if(i++ > (maxWaitMillis / 10)){
                throw new IllegalStateException("Waited more than "+maxWaitMillis+" millis");
            }
        }
        LOG.info("Waited for consume done! found {} records", consumeCounter.get());

    }

    public void resetCounter() {
        consumeCounter.set(0);
    }
}

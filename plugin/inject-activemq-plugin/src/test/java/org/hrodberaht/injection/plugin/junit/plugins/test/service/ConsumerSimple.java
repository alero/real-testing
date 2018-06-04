package org.hrodberaht.injection.plugin.junit.plugins.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ConsumerSimple {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerSimple.class);

    private CountDownLatch latch;
    private List<String> messages;

    public CountDownLatch getLatch() {
        return latch;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
        messages = new ArrayList<>();
    }

    @JmsListener(destination = "testqueue")
    public void receive(String message) {
        LOGGER.info("{} received message='{}'", this, message);
        messages.add(message);
        latch.countDown();
    }
}

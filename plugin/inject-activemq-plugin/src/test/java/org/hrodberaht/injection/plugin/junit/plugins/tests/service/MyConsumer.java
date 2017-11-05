package org.hrodberaht.injection.plugin.junit.plugins.tests.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MyConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyConsumer.class);

    private CountDownLatch latch;
    private List<String> messages = new ArrayList<>();

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    @JmsListener(destination = "testqueue")
    public void receive(String message) {
        LOGGER.info("{} received message='{}'", this, message);
        messages.add(message);
        latch.countDown();
    }
}

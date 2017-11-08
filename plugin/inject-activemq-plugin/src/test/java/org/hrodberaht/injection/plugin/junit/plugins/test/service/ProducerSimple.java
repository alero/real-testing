package org.hrodberaht.injection.plugin.junit.plugins.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.inject.Inject;
import java.util.concurrent.CountDownLatch;

public class ProducerSimple {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerSimple.class);

    private final JmsTemplate jmsTemplate;
    private final ConsumerSimple consumerSimple;

    @Inject
    public ProducerSimple(JmsTemplate jmsTemplate, ConsumerSimple consumerSimple) {
        this.jmsTemplate = jmsTemplate;
        this.consumerSimple = consumerSimple;
    }

    public void send(String destination, String message) {
        consumerSimple.setLatch(new CountDownLatch(1));
        LOGGER.info("sending message='{}' to destination='{}'", message, destination);
        jmsTemplate.convertAndSend(destination, message);
    }


}

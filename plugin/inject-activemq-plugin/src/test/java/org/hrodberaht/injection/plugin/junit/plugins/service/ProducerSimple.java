package org.hrodberaht.injection.plugin.junit.plugins.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.inject.Inject;

public class ProducerSimple {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerSimple.class);

    @Inject
    private JmsTemplate jmsTemplate;

    public void send(String destination, String message) {
        LOGGER.info("sending message='{}' to destination='{}'", message, destination);
        jmsTemplate.convertAndSend(destination, message);
    }


}

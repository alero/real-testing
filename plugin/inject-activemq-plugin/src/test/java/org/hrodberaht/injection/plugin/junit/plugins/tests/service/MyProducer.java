package org.hrodberaht.injection.plugin.junit.plugins.tests.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.inject.Inject;
import java.util.concurrent.CountDownLatch;

public class MyProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyConsumer.class);

    private final JmsTemplate jmsTemplate;
    private final MyConsumer myConsumer;


    @Inject
    public MyProducer(JmsTemplate jmsTemplate, MyConsumer myConsumer) {
        this.jmsTemplate = jmsTemplate;
        this.myConsumer = myConsumer;
    }

    public void send(String destination, String message) {
        myConsumer.setLatch(new CountDownLatch(1));
        LOGGER.info("sending message='{}' to destination='{}'", message, destination);
        jmsTemplate.convertAndSend(destination, message);
    }


}

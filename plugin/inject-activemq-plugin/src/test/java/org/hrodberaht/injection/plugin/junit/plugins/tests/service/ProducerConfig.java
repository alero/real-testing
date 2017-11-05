package org.hrodberaht.injection.plugin.junit.plugins.tests.service;

import org.springframework.context.annotation.Bean;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

public class ProducerConfig {


    public CachingConnectionFactory cachingConnectionFactory(ConnectionFactory connectionFactory) {
        return new CachingConnectionFactory(connectionFactory);
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(cachingConnectionFactory(connectionFactory));
    }

    @Bean
    public MyProducer producerSimple(JmsTemplate jmsTemplate, MyConsumer myConsumer) {
        return new MyProducer(jmsTemplate, myConsumer);
    }

}

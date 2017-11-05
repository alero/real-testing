package org.hrodberaht.injection.plugin.junit.plugins.test.spring;

import org.hrodberaht.injection.plugin.junit.plugins.test.service.ConsumerSimple;
import org.hrodberaht.injection.plugin.junit.plugins.test.service.ProducerSimple;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

public class ProducerSimpleConfig {


    public CachingConnectionFactory cachingConnectionFactory(ConnectionFactory connectionFactory) {
        return new CachingConnectionFactory(connectionFactory);
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(cachingConnectionFactory(connectionFactory));
    }

    @Bean
    public ProducerSimple producerSimple(JmsTemplate jmsTemplate, ConsumerSimple consumerSimple) {
        return new ProducerSimple(jmsTemplate, consumerSimple);
    }

}

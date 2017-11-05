package org.hrodberaht.injection.plugin.junit.plugins.test.spring;

import org.hrodberaht.injection.plugin.junit.plugins.test.service.ConsumerSimple;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class ConsumerSimpleConfig {

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("3-10");
        return factory;
    }

    @Bean
    public ConsumerSimple receiver() {
        return new ConsumerSimple();
    }


}

package org.hrodberaht.injection.plugin.junit.spring.beans.config;

import org.hrodberaht.injection.plugin.junit.spring.beans.ApplicationContextService;
import org.hrodberaht.injection.plugin.junit.spring.beans.SpringInjectionBeanInjector;
import org.hrodberaht.injection.plugin.junit.spring.beans.incubator.ReplacementBeans;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by robertalexandersson on 4/14/16.
 */
@Configuration
public class ContainerSpringConfig {

    @Bean
    public ApplicationContextService applicationContextService(){
        return new ApplicationContextService();
    }

    @Bean
    public SpringInjectionBeanInjector springInjectionBeanInjector(){
        return new SpringInjectionBeanInjector();
    }

}

package org.hrodberaht.injection.plugin.junit.spring.beans.config;

import org.hrodberaht.injection.plugin.junit.spring.beans.SpringEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by robertalexandersson on 4/14/16.
 */
@Configuration
public class ContainerJPASpringConfig {

    @Bean
    public SpringEntityManager springEntityManager(){
        return new SpringEntityManager();
    }
}

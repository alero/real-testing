package org.hrodberaht.injection.extensions.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by robertalexandersson on 4/14/16.
 */
@Configuration
@ComponentScan(basePackages = "org.hrodberaht.injection.extensions.spring.services")
public class ContainerSpringConfig {

}

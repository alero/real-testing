package org.hrodberaht.injection.extensions.spring.config;

import org.springframework.context.annotation.Import;

/**
 * Created by robertalexandersson on 4/14/16.
 */
@Import(value = {ContainerJPASpringConfig.class, ContainerSpringConfig.class})
public class ContainerAllSpringConfig {

}

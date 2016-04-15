package org.hrodberaht.injection.extensions.spring.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.spring.instance.SpringInject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import static org.hrodberaht.injection.extensions.spring.config.SpringConfigJavaComboSample._package;

/**
 * Created by robertalexandersson on 4/14/16.
 */
@Configuration
@ComponentScan(basePackages = _package)
@ImportResource(locations = "/META-INF/spring-config-datasource.xml")
public class SpringConfigJavaComboSample {

    protected static final String _package = "org.hrodberaht.injection.extensions.spring.testservices.spring";

    @SpringInject
    private InjectContainer injectContainer;

    @Bean(name = "replacementBeans")
    public ReplacementBeans getReplacementBeans() {
        return new ReplacementBeans(injectContainer, _package);
    }


}

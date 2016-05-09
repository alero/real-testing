package org.hrodberaht.injection.extensions.spring.junit.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.spring.instance.SpringInject;
import org.hrodberaht.injection.extensions.spring.junit.ReplacementBeans;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.PlatformTransactionManager;

import static org.hrodberaht.injection.extensions.spring.junit.config.SpringConfigJavaComboSample._package;


/**
 * Created by robertalexandersson on 4/14/16.
 */
@Configuration
@ComponentScan(basePackages = _package)
@ImportResource(locations = "/META-INF/junit/spring-config-datasource.xml")
public class SpringConfigJavaComboSample {

    protected static final String _package = "org.hrodberaht.injection.extensions.spring.junit.testservices";

    @SpringInject
    private InjectContainer injectContainer;

    @Bean(name = "replacementBeans")
    public ReplacementBeans getReplacementBeans() {
        return new ReplacementBeans(injectContainer, _package);
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return Mockito.mock(PlatformTransactionManager.class);
    }

}

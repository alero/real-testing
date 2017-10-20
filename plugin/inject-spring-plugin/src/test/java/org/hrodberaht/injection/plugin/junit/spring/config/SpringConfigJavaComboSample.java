package org.hrodberaht.injection.plugin.junit.spring.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.plugin.junit.spring.beans.incubator.ReplacementBeans;
import org.hrodberaht.injection.plugin.junit.spring.injector.SpringInject;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.PlatformTransactionManager;

import static org.hrodberaht.injection.plugin.junit.spring.config.SpringConfigJavaComboSample._package;


/**
 * Created by robertalexandersson on 4/14/16.
 */
@Configuration
@ComponentScan(basePackages = _package)
@ImportResource(locations = "/META-INF/spring-config-datasource.xml")
public class SpringConfigJavaComboSample {

    static final String _package = "org.hrodberaht.injection.plugin.junit.spring.testservices.spring";

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

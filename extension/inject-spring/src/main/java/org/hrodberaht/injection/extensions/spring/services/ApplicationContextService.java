package org.hrodberaht.injection.extensions.spring.services;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by robertalexandersson on 4/14/16.
 */
@Component
public class ApplicationContextService implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void refresh() {
        ((ConfigurableApplicationContext) applicationContext).close();
        ((ConfigurableApplicationContext) applicationContext).refresh();
    }
}

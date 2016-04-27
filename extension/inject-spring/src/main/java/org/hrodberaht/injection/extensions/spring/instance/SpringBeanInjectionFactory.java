package org.hrodberaht.injection.extensions.spring.instance;

import org.hrodberaht.injection.register.InjectionFactory;
import org.springframework.context.ApplicationContext;

public class SpringBeanInjectionFactory implements InjectionFactory {

    private Class springClass;
    private ApplicationContext applicationContext;

    public SpringBeanInjectionFactory(Class springClass, ApplicationContext applicationContext) {
        this.springClass = springClass;
        this.applicationContext = applicationContext;
    }

    @Override
    public Object getInstance() {
        return applicationContext.getBean(springClass);
    }

    @Override
    public Class getInstanceType() {
        return springClass;
    }

    @Override
    public boolean newObjectOnInstance() {
        return false;
    }
}

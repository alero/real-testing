package org.hrodberaht.injection.plugin.junit.spring.testservices.simple;

import org.hrodberaht.injection.plugin.junit.spring.testservices.spring.SpringBeanQualifierInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by alexbrob on 2016-03-29.
 */
public class ServiceForQualifier {

    @Autowired
    private SpringBeanQualifierInterface springBeanInterface;

    @Autowired
    @Qualifier(value = "QImpl2")
    private SpringBeanQualifierInterface springBeanInterface2;

    public SpringBeanQualifierInterface getSpringBeanInterface() {
        return springBeanInterface;
    }

    public SpringBeanQualifierInterface getSpringBeanInterface2() {
        return springBeanInterface2;
    }
}

package org.hrodberaht.injection.extensions.spring.testservices.simple;

import org.hrodberaht.injection.extensions.spring.testservices.spring.SpringBeanInterface;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by alexbrob on 2016-03-29.
 */
public class ServiceForQualifier {

    @Autowired
    private SpringBeanInterface springBeanInterface;

    @Autowired
    private SpringBeanInterface springBeanInterface2;

    public SpringBeanInterface getSpringBeanInterface() {
        return springBeanInterface;
    }

    public SpringBeanInterface getSpringBeanInterface2() {
        return springBeanInterface2;
    }
}

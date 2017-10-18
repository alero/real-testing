package org.hrodberaht.injection.plugin.junit.spring.testservices.simple;


import org.hrodberaht.injection.plugin.junit.spring.testservices.spring.SpringBeanInterface;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by alexbrob on 2016-03-29.
 */
public class ServiceForInterface {

    @Autowired
    private SpringBeanInterface springBeanInterface;

    public SpringBeanInterface getSpringBeanInterface() {
        return springBeanInterface;
    }


}

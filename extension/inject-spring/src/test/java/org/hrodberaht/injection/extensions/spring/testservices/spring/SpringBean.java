package org.hrodberaht.injection.extensions.spring.testservices.spring;

import org.hrodberaht.injection.extensions.spring.testservices.simple.AnyServiceInner;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by alexbrob on 2016-03-29.
 */
@Component
public class SpringBean {

    @Inject
    private AnyServiceInner anyServiceInner;

    public String getName(){
        return "SpringBeanName";
    }



}

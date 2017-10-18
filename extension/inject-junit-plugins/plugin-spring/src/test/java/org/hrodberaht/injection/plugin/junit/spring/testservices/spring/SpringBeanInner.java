package org.hrodberaht.injection.plugin.junit.spring.testservices.spring;

import org.hrodberaht.injection.plugin.junit.spring.injector.SpringInject;
import org.hrodberaht.injection.plugin.junit.spring.testservices.simple.AnyServiceInner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by alexbrob on 2016-03-29.
 */
@Component (value = "SpringBeanInnerNamed")
public class SpringBeanInner {

    private AnyServiceInner anyServiceInner;

    @Autowired
    private SpringBeanInner2 springBeanInner2;

    public String getName(){
        return "SpringBeanInnerName";
    }

    public AnyServiceInner getAnyServiceInner() {
        return anyServiceInner;
    }

    @SpringInject
    public void setAnyServiceInner(AnyServiceInner anyServiceInner) {
        this.anyServiceInner = anyServiceInner;
    }

    public SpringBeanInner2 getSpringBeanInner2() {
        return springBeanInner2;
    }
}

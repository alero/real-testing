package org.hrodberaht.injection.extensions.spring.junit.testservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by alexbrob on 2016-03-29.
 */
@Component(value = "SpringBeanInnerNamed")
public class SpringBeanInner {


    @Autowired
    private SpringBeanInner2 springBeanInner2;

    public String getName() {
        return "SpringBeanInnerName";
    }

    public SpringBeanInner2 getSpringBeanInner2() {
        return springBeanInner2;
    }
}

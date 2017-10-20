package org.hrodberaht.injection.plugin.junit.spring.testservices.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by alexbrob on 2016-03-29.
 */
@Component
public class SpringBeanInner2 {


    @Autowired
    private SpringBean springBean;

    public String getName() {
        return "SpringBeanInnerName";
    }

    public SpringBean getSpringBean() {
        return springBean;
    }
}

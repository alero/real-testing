package org.hrodberaht.injection.extensions.spring.junit.testservices;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Created by alexbrob on 2016-04-01.
 */
@Primary
@Component(value = "QImpl1")
public class SpringBeanQualifierInterfaceImpl1 implements SpringBeanQualifierInterface {
}

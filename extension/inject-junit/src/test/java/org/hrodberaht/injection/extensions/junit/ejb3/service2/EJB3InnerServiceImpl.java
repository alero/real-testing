package org.hrodberaht.injection.extensions.junit.ejb3.service2;

import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:29:52
 * @version 1.0
 * @since 1.0
 */
@Stateless
@Local(value = EJB3InnerServiceInterface.class)
public class EJB3InnerServiceImpl implements EJB3InnerServiceInterface {


    public void doSomething() {
        System.out.print("Hi there Inner");
    }

    public String findSomething(Long id) {
        return "Something Deep";
    }


}

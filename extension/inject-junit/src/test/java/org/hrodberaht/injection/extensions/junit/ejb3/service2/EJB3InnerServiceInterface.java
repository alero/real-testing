package org.hrodberaht.injection.extensions.junit.ejb3.service2;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:28:55
 * @version 1.0
 * @since 1.0
 */
public interface EJB3InnerServiceInterface {
    void doSomething();

    String findSomething(Long id);

}

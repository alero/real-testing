package org.hrodberaht.injection.extensions.junit.ejb3.service2;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:24:39
 * @version 1.0
 * @since 1.0
 */
public interface EJB3ServiceInterface {
    void doSomething();

    String findSomething(Long id);

    void doSomethingDeep();

    String findSomethingDeep(Long id);

}

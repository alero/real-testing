package test.org.hrodberaht.inject.extension.ejbunit.ejb3.service;

import test.org.hrodberaht.inject.extension.ejbunit.common.SomeData;

import javax.ejb.Local;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:28:55
 * @version 1.0
 * @since 1.0
 */
@Local
public interface EJB3InnerServiceInterface {
    void doSomething();

    String findSomething(Long id);

    String findSomethingFromDataSource(Long id);

    String findSomethingFromDataSource2(Long id);

    String findSomethingFromEntityManager(Long id);

    void updateSomethingInDataSource(Long id, String name);

    SomeData createSomethingForEntityManager(SomeData someData);
}

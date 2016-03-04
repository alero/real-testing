package test.org.hrodberaht.inject.extension.ejbunit.spring.service;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:28:55
 * @version 1.0
 * @since 1.0
 */
public interface NamedSpringServiceInterface {
    void doSomething();

    String findSomething(Long id);

    String findSomethingFromDataSource(Long id);

    void updateSomethingInDataSource(Long id, String name);
}

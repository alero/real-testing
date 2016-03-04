package test.org.hrodberaht.inject.extension.ejbunit.ejb3.service2;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:24:39
 * @version 1.0
 * @since 1.0
 */
public interface EJB3ServiceInterfaceRemote {
    String findSomething(Long id);

    String findSomethingDeep(Long id);

}

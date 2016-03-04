package test.org.hrodberaht.inject.extension.ejbunit.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:26:59
 * @version 1.0
 * @since 1.0
 */

@Component
public class SpringServiceImpl implements SpringServiceInterface {


    @Autowired
    private SpringInnerServiceInterface innerService;

    @Autowired
    @Qualifier(value = "WithAName")
    private NamedSpringServiceInterface namedService;


    public void doSomething() {
        System.out.println("Hi");
    }

    public String findSomething(Long id) {
        return "Something";
    }

    public void doSomethingDeep() {
        innerService.doSomething();
    }

    public String findSomethingDeep(Long id) {
        return innerService.findSomething(id);
    }

    public String findSomethingDeepWithDataSource(Long id) {
        return innerService.findSomethingFromDataSource(id);
    }

    public void updateSomethingInDataSource(Long id, String name) {
        innerService.updateSomethingInDataSource(id, name);
    }
}

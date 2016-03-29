package org.hrodberaht.inject.extensions.junit.ejb3.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:26:59
 * @version 1.0
 * @since 1.0
 */

@Stateless
public class EJB3ServiceImpl implements EJB3ServiceInterface {


    @EJB
    private EJB3InnerServiceInterface innerService;

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

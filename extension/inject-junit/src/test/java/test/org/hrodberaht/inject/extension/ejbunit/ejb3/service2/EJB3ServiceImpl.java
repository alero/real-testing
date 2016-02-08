package test.org.hrodberaht.inject.extension.ejbunit.ejb3.service2;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

/**
 * Unit Test EJB (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:26:59
 * @version 1.0
 * @since 1.0
 */

@Stateless
@Local(value = EJB3ServiceInterface.class)
@Remote(value = EJB3ServiceInterfaceRemote.class)
public class EJB3ServiceImpl implements EJB3ServiceInterface, EJB3ServiceInterfaceRemote {


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
}

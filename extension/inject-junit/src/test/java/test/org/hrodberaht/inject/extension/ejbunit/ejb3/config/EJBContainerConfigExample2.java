package test.org.hrodberaht.inject.extension.ejbunit.ejb3.config;

import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.extension.tdd.ejb.TDDEJBContainerConfigBase;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:37:42
 * @version 1.0
 * @since 1.0
 */
public class EJBContainerConfigExample2 extends TDDEJBContainerConfigBase {

    public EJBContainerConfigExample2() {

    }


    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("test.org.hrodberaht.inject.extension.ejbunit.ejb3.service2");
    }


}

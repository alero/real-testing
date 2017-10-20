package org.hrodberaht.injection.extensions.junit.ejb3.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.junit.ejb.TDDEJBContainerConfigBase;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:37:42
 * @version 1.0
 * @since 1.0
 */
public class EJBContainerConfigExample2 extends TDDEJBContainerConfigBase {

    public EJBContainerConfigExample2() {

    }


    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("org.hrodberaht.injection.extensions.junit.ejb3.service2");
    }


}

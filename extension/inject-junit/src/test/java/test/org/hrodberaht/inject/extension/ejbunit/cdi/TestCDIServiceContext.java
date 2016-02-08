package test.org.hrodberaht.inject.extension.ejbunit.cdi;

import org.hrodberaht.inject.extension.tdd.ContainerContext;
import org.hrodberaht.inject.extension.tdd.JUnitRunner;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import test.org.hrodberaht.inject.extension.ejbunit.ejb3.config.EJBContainerConfigExample;

/**
 * Unit Test EJB (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:31:30
 * @version 1.0
 * @since 1.0
 */
@ContainerContext(EJBContainerConfigExample.class)
@RunWith(JUnitRunner.class)
@Ignore
public class TestCDIServiceContext {


}

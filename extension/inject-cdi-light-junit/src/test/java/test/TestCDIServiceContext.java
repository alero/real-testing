package test;

import org.hrodberaht.injection.extensions.junit.ContainerContext;
import org.hrodberaht.injection.extensions.junit.JUnitRunner;
import org.hrodberaht.injection.extensions.junit.util.ContainerLifeCycleTestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.config.CDIContainerConfigExample;
import test.service.CDIServiceInterface;
import test.service.SimpleService;
import test.service.SimpleServiceSingleton;

import javax.inject.Inject;
import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:31:30
 * @version 1.0
 * @since 1.0
 */
@ContainerContext(CDIContainerConfigExample.class)
@RunWith(JUnitRunner.class)
public class TestCDIServiceContext {


    @Inject
    private CDIServiceInterface anInterface;

    @Inject
    private ContainerLifeCycleTestUtil containerLifeCycleTestUtil;

    @Test
    public void testWiring() {
        String something = anInterface.findSomething(12L);
        assertEquals("Something", something);

        String somethingDeep = anInterface.findSomethingDeep(12L);
        assertEquals("initialized", somethingDeep);

        DataSource dataSource = anInterface.getDataSource();
        assertNotNull(dataSource);

        assertTrue(
                containerLifeCycleTestUtil.getService(CDIServiceInterface.class)
                    ==
                containerLifeCycleTestUtil.getService(CDIServiceInterface.class)
        );

        assertTrue(
                containerLifeCycleTestUtil.getService(CDIServiceInterface.class).getLoadedPostContainer()
                    ==
                containerLifeCycleTestUtil.getService(CDIServiceInterface.class).getLoadedPostContainer()
        );

        assertTrue(
                containerLifeCycleTestUtil.getService(SimpleServiceSingleton.class)
                    ==
                containerLifeCycleTestUtil.getService(SimpleServiceSingleton.class)
        );

        assertFalse(
                containerLifeCycleTestUtil.getService(SimpleService.class)
                    ==
                containerLifeCycleTestUtil.getService(SimpleService.class)
        );

    }

}

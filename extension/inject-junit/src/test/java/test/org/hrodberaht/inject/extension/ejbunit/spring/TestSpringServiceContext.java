package test.org.hrodberaht.inject.extension.ejbunit.spring;

import org.hrodberaht.inject.extension.tdd.ContainerContext;
import org.hrodberaht.inject.extension.tdd.ContainerLifeCycleTestUtil;
import org.hrodberaht.inject.extension.tdd.JUnitRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import test.org.hrodberaht.inject.extension.ejbunit.spring.config.SpringContainerConfigExample;
import test.org.hrodberaht.inject.extension.ejbunit.spring.service.SpringInnerServiceInterface;
import test.org.hrodberaht.inject.extension.ejbunit.spring.service.SpringServiceInterface;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * Injection Extension JUnit
 *
 * @author Robert Alexandersson
 *         2010-okt-26 18:56:45
 * @version 1.0
 * @since 1.0
 */
@ContainerContext(SpringContainerConfigExample.class)
@RunWith(JUnitRunner.class)
@Ignore
public class TestSpringServiceContext {

    @Inject
    private SpringServiceInterface serviceInterfaceWithInject;

    @Autowired
    private SpringServiceInterface anInterface;

    @Test
    public void testWiring() {
        String something = anInterface.findSomething(12L);
        assertEquals("Something", something);

        String somethingDeep = anInterface.findSomethingDeep(12L);
        assertEquals("Something Deep", somethingDeep);
    }

    @Test
    public void testInjectWiring() {
        String something = serviceInterfaceWithInject.findSomething(12L);
        assertEquals("Something", something);

        String somethingDeep = serviceInterfaceWithInject.findSomethingDeep(12L);
        assertEquals("Something Deep", somethingDeep);
    }

    @Test
    public void testResourceInjectionAndUpdate() {
        String something = anInterface.findSomethingDeepWithDataSource(12L);
        assertEquals("The Name", something);
        anInterface.updateSomethingInDataSource(12L, "A new Name");
        something = anInterface.findSomethingDeepWithDataSource(12L);
        assertEquals("A new Name", something);
    }

    @Test
    public void testNamedResourceInjectionAndUpdate() {
        String something = anInterface.findSomethingDeepWithDataSource(12L);
        assertEquals("The Name", something);
        anInterface.updateSomethingInDataSource(12L, "A new Name");
        something = anInterface.findSomethingDeepWithDataSource(12L);
        assertEquals("A new Name", something);
    }

    @Test
    public void testResourceInjection() {
        String something = serviceInterfaceWithInject.findSomethingDeepWithDataSource(12L);
        assertEquals("The Name", something);
    }


    @Test
    public void testReWiringWithMockito() {

        SpringInnerServiceInterface anInterface = Mockito.mock(SpringInnerServiceInterface.class);
        Mockito.when(anInterface.findSomething(12L)).thenReturn("Something Deep From Mock");
        ContainerLifeCycleTestUtil.registerServiceInstance(SpringInnerServiceInterface.class, anInterface);

        SpringServiceInterface serviceInterface = ContainerLifeCycleTestUtil.getService(SpringServiceInterface.class);
        String something = serviceInterface.findSomething(12L);
        assertEquals("Something", something);

        String somethingDeep = serviceInterface.findSomethingDeep(12L);
        assertEquals("Something Deep From Mock", somethingDeep);
    }

}

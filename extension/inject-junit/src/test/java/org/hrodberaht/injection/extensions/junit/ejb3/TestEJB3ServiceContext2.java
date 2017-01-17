package org.hrodberaht.injection.extensions.junit.ejb3;

import org.hrodberaht.injection.extensions.junit.common.SomeData;
import org.hrodberaht.injection.extensions.junit.ejb3.config.EJBContainerConfigExample;
import org.hrodberaht.injection.extensions.junit.ejb3.config.MockedInnerModule;
import org.hrodberaht.injection.extensions.junit.ejb3.service.EJB3InnerServiceInterface;
import org.hrodberaht.injection.extensions.junit.ejb3.service.EJB3ServiceInterface;
import org.hrodberaht.injection.extensions.junit.ContainerContext;
import org.hrodberaht.injection.extensions.junit.JUnitRunner;
import org.hrodberaht.injection.extensions.junit.util.ContainerLifeCycleTestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.ejb.EJB;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:31:30
 * @version 1.0
 * @since 1.0
 */
@ContainerContext(EJBContainerConfigExample.class)
@RunWith(JUnitRunner.class)
public class TestEJB3ServiceContext2 {


    @EJB
    private EJB3ServiceInterface ejb3ServiceInterfaceWithInject;

    @EJB
    private EJB3ServiceInterface anInterface;

    @EJB
    private EJB3InnerServiceInterface ejb3InnerServiceInterface;

    @Inject
    private ContainerLifeCycleTestUtil containerLifeCycleTestUtil;

    @Test
    public void testEJBWiring() {
        String something = anInterface.findSomething(12L);
        assertEquals("Something", something);

        String somethingDeep = anInterface.findSomethingDeep(12L);
        assertEquals("Something Deep", somethingDeep);
    }

    @Test
    public void testEJBInjectWiring() {
        String something = ejb3ServiceInterfaceWithInject.findSomething(12L);
        assertEquals("Something", something);

        String somethingDeep = ejb3ServiceInterfaceWithInject.findSomethingDeep(12L);
        assertEquals("Something Deep", somethingDeep);
    }

    @Test
    public void testEJBResourceInjectionAndUpdate() {
        String something = anInterface.findSomethingDeepWithDataSource(12L);
        assertEquals("The Name", something);
        anInterface.updateSomethingInDataSource(12L, "A new Name");
        something = anInterface.findSomethingDeepWithDataSource(12L);
        assertEquals("A new Name", something);
    }

    @Test
    public void testEJBResourceInjection() {
        String something = ejb3ServiceInterfaceWithInject.findSomethingDeepWithDataSource(12L);
        assertEquals("The Name", something);

    }

    @Test
    public void testEJBTypedResourceInjection() {
        String something = ejb3InnerServiceInterface.findSomethingFromDataSource2(12L);
        assertEquals("The Name", something);

    }

    @Test
    public void testEJBEntityManagerInjection() {
        String something = ejb3InnerServiceInterface.findSomethingFromEntityManager(12L);
        assertEquals("The Name", something);

    }

    @Test
    public void testEJBEntityManagerInjectionCreate() {
        SomeData someData = new SomeData();
        someData.setName("The Name");
        someData.setId(13L);
        someData = ejb3InnerServiceInterface.createSomethingForEntityManager(someData);
        assertEquals("The Name", someData.getName());
    }

    @Test
    public void testEJBEntityManagerInjectionCreate2() {
        SomeData someData = new SomeData();
        someData.setName("The Name");
        someData.setId(13L);
        someData = ejb3InnerServiceInterface.createSomethingForEntityManager(someData);
        assertEquals("The Name", someData.getName());
    }

    @Test
    public void testEJBEntityManagerInjectionCreateDuplicate() {
        // Verifies transaction rollback handling
        testEJBEntityManagerInjectionCreate();
    }


    @Test
    public void testEJBWiringWithMockito() {

        EJB3InnerServiceInterface anInterface = Mockito.mock(EJB3InnerServiceInterface.class);
        Mockito.when(anInterface.findSomething(12L)).thenReturn("Something Deep From Mock");
        containerLifeCycleTestUtil.registerServiceInstance(EJB3InnerServiceInterface.class, anInterface);

        EJB3ServiceInterface serviceInterface = containerLifeCycleTestUtil.getService(EJB3ServiceInterface.class);
        String something = serviceInterface.findSomething(12L);
        assertEquals("Something", something);

        String somethingDeep = serviceInterface.findSomethingDeep(12L);
        assertEquals("Something Deep From Mock", somethingDeep);
    }

    @Test
    public void testModuleRegistration() {
        containerLifeCycleTestUtil.registerModule(new MockedInnerModule());

        EJB3ServiceInterface serviceInterface = containerLifeCycleTestUtil.getService(EJB3ServiceInterface.class);
        String something = serviceInterface.findSomethingDeep(12L);
        assertEquals("Mocked", something);

    }

}

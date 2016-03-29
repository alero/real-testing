package org.hrodberaht.inject.extensions.junit.demo.test;

import org.hrodberaht.inject.extensions.junit.demo.model.Customer;
import org.hrodberaht.inject.extensions.junit.demo.service.CustomerService;
import org.hrodberaht.inject.extensions.junit.demo.test.config.CourseContainerConfigExample;
import org.hrodberaht.injection.extensions.tdd.ContainerContext;
import org.hrodberaht.injection.extensions.tdd.JUnitRunner;
import org.hrodberaht.injection.extensions.tdd.util.JPATestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.inject.Inject;

import static org.junit.Assert.*;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-05-03 20:05
 * @created 1.0
 * @since 1.0
 */
@ContainerContext(CourseContainerConfigExample.class)
@RunWith(JUnitRunner.class)
public class TestCustomerServicePersistence {


    @EJB
    private CustomerService customerService;

    @Inject
    JPATestUtil jpaTestUtil;

    /**
     * Test Persistence create/read
     */
    @Test
    public void testCustomerCreateRead() {
        // Create the customer and save in DB
        Customer customer = CourseDataModelStub.createCustomer();
        customerService.create(customer);
        // Flush the JPA session to force insert and clear first lvl cache for reselect
        jpaTestUtil.flushAndClear();
        // Find the customer from DB, new instance means recreated from db
        Customer foundCustomer = customerService.find(customer.getId());

        // Assert functionality
        assertNotSame(customer, foundCustomer);
        assertEquals(customer.getId(), foundCustomer.getId());
        assertEquals(customer.getName(), foundCustomer.getName());

    }

    /**
     * Test Persistence read from pre-created values (via insert script 'insert_script_customer.sql')
     */
    @Test
    public void testCustomerRead() {

        Customer foundCustomer = customerService.find(-1L);

        assertNotNull(foundCustomer);
        assertEquals("The Dude", foundCustomer.getName());

    }


}

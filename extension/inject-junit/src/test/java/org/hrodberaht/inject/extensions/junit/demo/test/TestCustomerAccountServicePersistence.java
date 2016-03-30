package org.hrodberaht.inject.extensions.junit.demo.test;

import org.hrodberaht.inject.extensions.junit.demo.model.Customer;
import org.hrodberaht.inject.extensions.junit.demo.model.CustomerAccount;
import org.hrodberaht.inject.extensions.junit.demo.service.CustomerAccountService;
import org.hrodberaht.inject.extensions.junit.demo.service.CustomerService;
import org.hrodberaht.inject.extensions.junit.demo.test.config.CourseContainerConfigExample;
import org.hrodberaht.injection.extensions.junit.ContainerContext;
import org.hrodberaht.injection.extensions.junit.JUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.assertEquals;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-05-03 21:42
 * @created 1.0
 * @since 1.0
 */
@ContainerContext(CourseContainerConfigExample.class)
@RunWith(JUnitRunner.class)
public class TestCustomerAccountServicePersistence {


    @EJB
    private CustomerAccountService customerAccountService;

    @EJB
    private CustomerService customerService;

    /**
     * Test Persistence create/read
     */
    @Test
    public void testCustomerCreateRead() {
        Customer customer = customerService.find(-1L);
        CustomerAccount customerAccount = CourseDataModelStub.createCustomerAccountEmpty(customer);

        customerAccountService.create(customerAccount);

        CustomerAccount foundCustomerAccount = customerAccountService.find(customerAccount.getId());

        assertEquals(customerAccount.getId(), foundCustomerAccount.getId());
        assertEquals(customerAccount.getAccountNumber(), foundCustomerAccount.getAccountNumber());

    }

}

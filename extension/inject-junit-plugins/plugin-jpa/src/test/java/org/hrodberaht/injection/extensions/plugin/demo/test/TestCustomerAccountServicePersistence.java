package org.hrodberaht.injection.extensions.plugin.demo.test;

import org.hrodberaht.injection.extensions.plugin.demo.model.Customer;
import org.hrodberaht.injection.extensions.plugin.demo.model.CustomerAccount;
import org.hrodberaht.injection.extensions.plugin.demo.service.CustomerAccountService;
import org.hrodberaht.injection.extensions.plugin.demo.service.CustomerService;
import org.hrodberaht.injection.extensions.plugin.demo.test.config.CourseContainerConfigExample;
import org.hrodberaht.injection.extensions.plugin.demo.test.util.CourseDataModelStub;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.PluggableJUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

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
@RunWith(PluggableJUnitRunner.class)
public class TestCustomerAccountServicePersistence {


    @Inject
    private CustomerAccountService customerAccountService;

    @Inject
    private CustomerService customerService;

    /**
     * Test Persistence register/read
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

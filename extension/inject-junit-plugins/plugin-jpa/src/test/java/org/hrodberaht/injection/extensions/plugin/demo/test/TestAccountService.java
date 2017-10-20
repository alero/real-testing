package org.hrodberaht.injection.extensions.plugin.demo.test;

import org.hrodberaht.injection.extensions.plugin.demo.model.Customer;
import org.hrodberaht.injection.extensions.plugin.demo.model.CustomerAccount;
import org.hrodberaht.injection.extensions.plugin.demo.service.AccountingService;
import org.hrodberaht.injection.extensions.plugin.demo.service.CustomerAccountService;
import org.hrodberaht.injection.extensions.plugin.demo.service.CustomerService;
import org.hrodberaht.injection.extensions.plugin.demo.test.util.AbstractBaseClass;
import org.hrodberaht.injection.extensions.plugin.demo.test.util.CourseDataModelStub;
import org.junit.Test;

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

public class TestAccountService extends AbstractBaseClass {

    @Inject
    private AccountingService accountingService;

    @Inject
    private CustomerAccountService customerAccountService;

    @Inject
    private CustomerService customerService;

    @Test
    public void testWiring() throws Exception {
        assertEquals("initiated", this.init);
    }

    @Test
    public void testAccountAddMoney() throws Exception {
        Customer customer = customerService.find(-1L);
        CustomerAccount customerAccount = CourseDataModelStub.createCustomerAccountEmpty(customer);

        customerAccountService.create(customerAccount);

        accountingService.addMoney(500D, customerAccount.getId());

        CustomerAccount foundCustomerAccount = customerAccountService.find(customerAccount.getId());

        assertEquals(new Double(500D), foundCustomerAccount.getMoney());

    }

}

package org.hrodberaht.injection.extensions.plugin.demo.test;

import org.hrodberaht.injection.extensions.plugin.demo.model.Customer;
import org.hrodberaht.injection.extensions.plugin.demo.model.CustomerAccount;
import org.hrodberaht.injection.extensions.plugin.demo.service.AccountingService;
import org.hrodberaht.injection.extensions.plugin.demo.service.CustomerAccountService;
import org.hrodberaht.injection.extensions.plugin.demo.service.CustomerService;
import org.hrodberaht.injection.extensions.plugin.demo.test.util.AbstractBaseClass;
import org.hrodberaht.injection.extensions.plugin.demo.test.util.CourseDataModelStub;
import org.hrodberaht.injection.plugin.junit.ContainerLifeCycleTestUtil;
import org.junit.Test;
import org.mockito.Mockito;

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
public class TestAccountServiceMocking extends AbstractBaseClass {

    @Inject
    private CustomerAccountService customerAccountService;

    @Inject
    private CustomerService customerService;

    @Inject
    private ContainerLifeCycleTestUtil containerLifeCycleTestUtil;

    @Test
    public void testAccountAddMoneyMockedUpdate() throws Exception {

        // Prepare data (no mocking active)
        Customer customer = customerService.find(-1L);
        CustomerAccount customerAccount = CourseDataModelStub.createCustomerAccountEmpty(customer);
        customerAccountService.create(customerAccount);

        // Register mocking
        CustomerAccountService customerAccountMock = Mockito.mock(CustomerAccountService.class);
        // Register prepared data as return
        Mockito.when(customerAccountMock.find(Mockito.anyLong())).thenReturn(customerAccount);
        containerLifeCycleTestUtil.registerServiceInstance(CustomerAccountService.class, customerAccountMock);

        // Re "fetch" the service intended for testing, all registration changes will be valid
        AccountingService accountingService = containerLifeCycleTestUtil.getService(AccountingService.class);
        accountingService.addMoney(500D, customerAccount.getId());

        // Asserting the functionality
        Mockito.verify(customerAccountMock).find(customerAccount.getId());
        Mockito.verify(customerAccountMock).update(Mockito.<CustomerAccount>any());

    }

    @Test
    public void testAccountAddMoneyMockedAdvancedUpdate() throws Exception {

        // Prepare data (no mocking active)
        Customer customer = customerService.find(-1L);
        final CustomerAccount customerAccount = CourseDataModelStub.createCustomerAccountEmpty(customer);
        // customerAccount.setMoney(5D);
        customerAccountService.create(customerAccount);
        // Register mocking
        final CustomerAccount[] customerAccountReturn = new CustomerAccount[1];
        CustomerAccountService customerAccountMock = new CustomerAccountService() {
            public CustomerAccount create(CustomerAccount customer) {
                return customer;
            }

            public CustomerAccount find(Long id) {
                return customerAccountService.find(id);
            }

            public CustomerAccount update(CustomerAccount customerAccount) {
                customerAccountReturn[0] = customerAccount;
                return customerAccount;
            }
        };
        // Register the Service
        containerLifeCycleTestUtil.registerServiceInstance(CustomerAccountService.class, customerAccountMock);

        // Re "fetch" the service intended for testing, all registration changes will be valid
        AccountingService accountingService = containerLifeCycleTestUtil.getService(AccountingService.class);
        accountingService.addMoney(500D, customerAccount.getId());

        // Asserting the functionality
        assertEquals(new Double(500D), customerAccountReturn[0].getMoney());

    }
}

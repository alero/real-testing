package test.org.hrodberaht.inject.extension.ejbunit.demo.test;

import org.hrodberaht.injection.extensions.tdd.ContainerContext;
import org.hrodberaht.injection.extensions.tdd.JUnitRunner;
import org.hrodberaht.injection.extensions.tdd.util.ContainerLifeCycleTestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import test.org.hrodberaht.inject.extension.ejbunit.demo.model.Customer;
import test.org.hrodberaht.inject.extension.ejbunit.demo.model.CustomerAccount;
import test.org.hrodberaht.inject.extension.ejbunit.demo.service.AccountingService;
import test.org.hrodberaht.inject.extension.ejbunit.demo.service.CustomerAccountService;
import test.org.hrodberaht.inject.extension.ejbunit.demo.service.CustomerService;
import test.org.hrodberaht.inject.extension.ejbunit.demo.test.config.CourseContainerConfigExample;

import javax.ejb.EJB;
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
@RunWith(JUnitRunner.class)
public class TestAccountServiceMocking {

    @EJB
    private CustomerAccountService customerAccountService;

    @EJB
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

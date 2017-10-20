package org.hrodberaht.injection.extensions.plugin.demo.test.util;

import org.hrodberaht.injection.extensions.plugin.demo.model.Customer;
import org.hrodberaht.injection.extensions.plugin.demo.model.CustomerAccount;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-05-03 20:19
 * @created 1.0
 * @since 1.0
 */
public class CourseDataModelStub {

    private static final AtomicLong aLong = new AtomicLong(0L);

    public static Customer createCustomer() {
        Customer customer = new Customer();
        customer.setId(aLong.incrementAndGet());
        customer.setName("John Doe");
        return customer;
    }

    public static CustomerAccount createCustomerAccountEmpty(Customer customer) {
        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setId(aLong.incrementAndGet());
        customerAccount.setAccountNumber("JD1000123123");
        customerAccount.setCustomer(customer);
        customerAccount.setMoney(0D);
        return customerAccount;
    }

}

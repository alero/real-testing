package test.org.hrodberaht.inject.extension.ejbunit.demo.service;

import test.org.hrodberaht.inject.extension.ejbunit.demo.model.Customer;

import javax.ejb.Local;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-05-03 20:24
 * @created 1.0
 * @since 1.0
 */
@Local
public interface CustomerService {
    Customer create(Customer customer);

    Customer find(Long id);
}

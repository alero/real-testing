package org.hrodberaht.inject.extensions.junit.demo.service;

import org.hrodberaht.inject.extensions.junit.demo.model.Customer;

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

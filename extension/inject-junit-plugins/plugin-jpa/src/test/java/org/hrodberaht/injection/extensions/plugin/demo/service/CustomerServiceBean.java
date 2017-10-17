package org.hrodberaht.injection.extensions.plugin.demo.service;

import org.hrodberaht.injection.extensions.plugin.demo.model.Customer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-05-03 20:24
 * @created 1.0
 * @since 1.0
 */
public class CustomerServiceBean implements CustomerService {

    @PersistenceContext(unitName = "example-jpa")
    protected EntityManager entityManager;


    public Customer create(Customer customer) {
        entityManager.persist(customer);
        return customer;
    }

    public Customer find(Long id) {
        return entityManager.find(Customer.class, id);
    }
}

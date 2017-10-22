package org.hrodberaht.injection.extensions.plugin.demo.service;

import org.hrodberaht.injection.extensions.plugin.demo.model.CustomerAccount;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 * 2011-05-03 21:43
 * @created 1.0
 * @since 1.0
 */
public class CustomerAccountServiceBean implements CustomerAccountService {

    @PersistenceContext(unitName = "example-jpa")
    protected EntityManager entityManager;

    public CustomerAccount find(Long id) {
        return entityManager.find(CustomerAccount.class, id);
    }

    @Override
    public CustomerAccount create(CustomerAccount customer) {
        entityManager.persist(customer);
        return customer;
    }

    @Override
    public CustomerAccount update(CustomerAccount customerAccount) {
        entityManager.persist(customerAccount);
        return customerAccount;
    }
}

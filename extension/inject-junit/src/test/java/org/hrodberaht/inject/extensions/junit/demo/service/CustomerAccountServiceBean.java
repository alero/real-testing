package org.hrodberaht.inject.extensions.junit.demo.service;

import org.hrodberaht.inject.extensions.junit.demo.model.CustomerAccount;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-05-03 21:43
 * @created 1.0
 * @since 1.0
 */
@Stateless
public class CustomerAccountServiceBean implements CustomerAccountService {

    @PersistenceContext(unitName = "example-jpa")
    protected EntityManager entityManager;

    public CustomerAccount find(Long id) {
        return entityManager.find(CustomerAccount.class, id);
    }

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public CustomerAccount create(CustomerAccount customer) {
        entityManager.persist(customer);
        return customer;
    }

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public CustomerAccount update(CustomerAccount customerAccount) {
        entityManager.persist(customerAccount);
        return customerAccount;
    }
}

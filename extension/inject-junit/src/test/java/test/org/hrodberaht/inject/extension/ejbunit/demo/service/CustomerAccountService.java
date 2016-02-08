package test.org.hrodberaht.inject.extension.ejbunit.demo.service;

import test.org.hrodberaht.inject.extension.ejbunit.demo.model.CustomerAccount;

import javax.ejb.Local;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-05-03 21:43
 * @created 1.0
 * @since 1.0
 */
@Local
public interface CustomerAccountService {

    CustomerAccount create(CustomerAccount customer);

    CustomerAccount find(Long id);

    CustomerAccount update(CustomerAccount customerAccount);
}

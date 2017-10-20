package org.hrodberaht.injection.extensions.junit.demo.service;

import org.hrodberaht.injection.extensions.junit.demo.model.CustomerAccount;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 * 2011-05-03 20:07
 * @created 1.0
 * @since 1.0
 */
@Stateless
public class AccountingServiceBean implements AccountingService {

    @EJB
    private CustomerAccountService customerAccountService;

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public void addMoney(double money, long customerAccountId) {
        CustomerAccount customerAccount = customerAccountService.find(customerAccountId);
        if (customerAccount.getMoney() != null) {
            customerAccount.setMoney(customerAccount.getMoney() + money);
        } else {
            customerAccount.setMoney(money);
        }

        customerAccountService.update(customerAccount);
    }
}

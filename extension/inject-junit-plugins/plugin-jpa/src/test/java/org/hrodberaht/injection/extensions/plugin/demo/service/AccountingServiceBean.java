package org.hrodberaht.injection.extensions.plugin.demo.service;


import org.hrodberaht.injection.extensions.plugin.demo.model.CustomerAccount;

import javax.inject.Inject;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-05-03 20:07
 * @created 1.0
 * @since 1.0
 */
public class AccountingServiceBean implements AccountingService {

    @Inject
    private CustomerAccountService customerAccountService;


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

/*
 * Copyright (c) 2017 org.hrodberaht
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hrodberaht.injection.extensions.plugin.demo.service;


import org.hrodberaht.injection.extensions.plugin.demo.model.CustomerAccount;

import javax.inject.Inject;

public class AccountingServiceBean implements AccountingService {

    @Inject
    private CustomerAccountService customerAccountService;

    @Override
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

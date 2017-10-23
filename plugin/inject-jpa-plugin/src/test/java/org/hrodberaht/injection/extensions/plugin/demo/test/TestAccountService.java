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

package org.hrodberaht.injection.extensions.plugin.demo.test;

import org.hrodberaht.injection.extensions.plugin.demo.model.Customer;
import org.hrodberaht.injection.extensions.plugin.demo.model.CustomerAccount;
import org.hrodberaht.injection.extensions.plugin.demo.service.AccountingService;
import org.hrodberaht.injection.extensions.plugin.demo.service.CustomerAccountService;
import org.hrodberaht.injection.extensions.plugin.demo.service.CustomerService;
import org.hrodberaht.injection.extensions.plugin.demo.test.util.AbstractBaseClass;
import org.hrodberaht.injection.extensions.plugin.demo.test.util.CourseDataModelStub;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

public class TestAccountService extends AbstractBaseClass {

    @Inject
    private AccountingService accountingService;

    @Inject
    private CustomerAccountService customerAccountService;

    @Inject
    private CustomerService customerService;

    @Test
    public void testWiring() throws Exception {
        assertEquals("initiated", this.init);
    }

    @Test
    public void testAccountAddMoney() throws Exception {
        Customer customer = customerService.find(-1L);
        CustomerAccount customerAccount = CourseDataModelStub.createCustomerAccountEmpty(customer);

        customerAccountService.create(customerAccount);

        accountingService.addMoney(500D, customerAccount.getId());

        CustomerAccount foundCustomerAccount = customerAccountService.find(customerAccount.getId());

        assertEquals(new Double(500D), foundCustomerAccount.getMoney());

    }

}

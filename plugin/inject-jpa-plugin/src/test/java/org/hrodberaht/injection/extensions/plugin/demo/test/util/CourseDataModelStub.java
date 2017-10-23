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

package org.hrodberaht.injection.extensions.plugin.demo.test.util;

import org.hrodberaht.injection.extensions.plugin.demo.model.Customer;
import org.hrodberaht.injection.extensions.plugin.demo.model.CustomerAccount;

import java.util.concurrent.atomic.AtomicLong;

public class CourseDataModelStub {

    private static final AtomicLong aLong = new AtomicLong(0L);

    public static Customer createCustomer() {
        Customer customer = new Customer();
        customer.setId(aLong.incrementAndGet());
        customer.setName("John Doe");
        return customer;
    }

    public static CustomerAccount createCustomerAccountEmpty(Customer customer) {
        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setId(aLong.incrementAndGet());
        customerAccount.setAccountNumber("JD1000123123");
        customerAccount.setCustomer(customer);
        customerAccount.setMoney(0D);
        return customerAccount;
    }

}

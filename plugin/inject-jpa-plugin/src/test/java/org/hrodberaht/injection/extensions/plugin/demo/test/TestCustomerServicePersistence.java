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
import org.hrodberaht.injection.extensions.plugin.demo.service.CustomerService;
import org.hrodberaht.injection.extensions.plugin.demo.test.config.CourseContainerConfigExample;
import org.hrodberaht.injection.extensions.plugin.demo.test.util.CourseDataModelStub;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnit4Runner;
import org.hrodberaht.injection.plugin.junit.jpa.JPATestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

@ContainerContext(CourseContainerConfigExample.class)
@RunWith(JUnit4Runner.class)
public class TestCustomerServicePersistence {


    @Inject
    private CustomerService customerService;

    @Inject
    JPATestUtil jpaTestUtil;

    /**
     * Test Persistence register/read
     */
    @Test
    public void testCustomerCreateRead() {
        // Create the customer and save in DB
        Customer customer = CourseDataModelStub.createCustomer();
        customerService.create(customer);
        // Flush the JPA session to force insert and clear first lvl cache for reselect
        jpaTestUtil.flushAndClear();
        // Find the customer from DB, new instance means recreated from db
        Customer foundCustomer = customerService.find(customer.getId());

        // Assert functionality
        assertNotSame(customer, foundCustomer);
        assertEquals(customer.getId(), foundCustomer.getId());
        assertEquals(customer.getName(), foundCustomer.getName());

    }

    /**
     * Test Persistence read from pre-created values (via insert script 'insert_script_customer.sql')
     */
    @Test
    public void testCustomerRead() {

        Customer foundCustomer = customerService.find(-1L);

        assertNotNull(foundCustomer);
        assertEquals("The Dude", foundCustomer.getName());

    }


}

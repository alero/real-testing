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

package org.hrodberaht.injection.plugin.junit.spring.test3;

import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnit4Runner;
import org.hrodberaht.injection.plugin.junit.spring.config.JUnitConfigExampleResourceToSpringBeans;
import org.hrodberaht.injection.plugin.junit.spring.testservices2.SpringBeanWithSpringBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContainerContext(JUnitConfigExampleResourceToSpringBeans.class)
@RunWith(JUnit4Runner.class)
public class TestJavaConfigOnlyInjectionQualifierMultiThreaded {

    @Autowired
    @Qualifier("springBean")
    private SpringBeanWithSpringBean springBean;


    @Test
    public void testWiredBeanResource() throws Exception {


        ExecutorService executor = Executors.newFixedThreadPool(10);
        AtomicLong callCounter = new AtomicLong(0);

        assertNotNull(springBean);

        assertNotNull(springBean.getName("dude"));

        assertEquals(new Integer(0), springBean.getLoginCount("dude"));

        int callers = 10;
        for(int i=0;i<callers;i++){
            executor.execute(() -> {
                springBean.login("dude", "wrong pass");
                callCounter.incrementAndGet();
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(new Integer(callCounter.intValue()), springBean.getLoginCount("dude"));

    }

}

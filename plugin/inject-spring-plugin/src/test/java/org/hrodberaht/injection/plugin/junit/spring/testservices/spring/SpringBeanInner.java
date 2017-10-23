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

package org.hrodberaht.injection.plugin.junit.spring.testservices.spring;

import org.hrodberaht.injection.plugin.junit.spring.injector.SpringInject;
import org.hrodberaht.injection.plugin.junit.spring.testservices.simple.AnyServiceInner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "SpringBeanInnerNamed")
public class SpringBeanInner {

    private AnyServiceInner anyServiceInner;

    @Autowired
    private SpringBeanInner2 springBeanInner2;

    public String getName() {
        return "SpringBeanInnerName";
    }

    public AnyServiceInner getAnyServiceInner() {
        return anyServiceInner;
    }

    @SpringInject
    public void setAnyServiceInner(AnyServiceInner anyServiceInner) {
        this.anyServiceInner = anyServiceInner;
    }

    public SpringBeanInner2 getSpringBeanInner2() {
        return springBeanInner2;
    }
}

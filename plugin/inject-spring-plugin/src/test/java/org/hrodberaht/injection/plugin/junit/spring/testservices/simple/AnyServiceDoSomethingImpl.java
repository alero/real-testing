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

package org.hrodberaht.injection.plugin.junit.spring.testservices.simple;/*
 * ~ Copyright (c) 2010.
 *   ~ Licensed under the Apache License, Version 2.0 (the "License");
 *   ~ you may not use this file except in compliance with the License.
 *   ~ You may obtain a copy of the License at
 *   ~
 *   ~        http://www.apache.org/licenses/LICENSE-2.0
 *   ~
 *   ~ Unless required by applicable law or agreed to in writing, software
 *   ~ distributed under the License is distributed on an "AS IS" BASIS,
 *   ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   ~ See the License for the specific language governing permissions and limitations under the License.
 */

import org.hrodberaht.injection.plugin.junit.spring.testservices.spring.SpringBean;
import org.hrodberaht.injection.plugin.junit.spring.testservices.spring.SpringBeanInner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;

public class AnyServiceDoSomethingImpl {


    @Autowired
    private SpringBean springBean;

    private SpringBeanInner springBeanInner;

    private Collection<String> collection = new ArrayList<String>();

    public void doStuff() {
        collection.add("Added something");
    }

    public Collection getStuff() {
        return collection;
    }

    public SpringBean getSpringBean() {
        return springBean;
    }

    public SpringBeanInner getSpringBeanInner() {
        return springBeanInner;
    }

    @Autowired
    public void setSpringBeanInner(SpringBeanInner springBeanInner) {
        this.springBeanInner = springBeanInner;
    }
}
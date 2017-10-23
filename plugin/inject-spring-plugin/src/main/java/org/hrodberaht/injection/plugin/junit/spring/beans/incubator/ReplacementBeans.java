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

package org.hrodberaht.injection.plugin.junit.spring.beans.incubator;

import org.hrodberaht.injection.core.InjectContainer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Component
public class ReplacementBeans {

    private InjectContainer injectContainer;

    private String[] packages;

    private Map<Class, Object> replacedBeans = new ConcurrentHashMap<>();

    public ReplacementBeans(InjectContainer injectContainer, String... packages) {
        this.packages = packages;
        this.injectContainer = injectContainer;
    }

    boolean createReplacementProxy(Object bean, String beanName) {
        if (bean != null) {
            String className = bean.getClass().getName();
            if (Stream.of(packages).anyMatch(className::contains)) {
                return true;
            }
        }
        return false;
    }

    Object getService(Class<?> clazz, Object bean) {
        if (replacedBeans.containsKey(clazz)) {
            return injectContainer.get(clazz);
        }
        return bean;
    }

    public void register(Class serviceDefinition, Object service) {
        replacedBeans.put(serviceDefinition, service);
    }
}

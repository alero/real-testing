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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.stereotype.Component;

import static org.springframework.cglib.core.TypeUtils.isFinal;

@Component
public class SpringBeanReplacementProxy implements BeanPostProcessor {

    @Autowired
    private ReplacementBeans replacementBeans;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
        Object result = bean;

        Class<?> clazz = result.getClass();
        if (!isFinal(clazz.getModifiers()) && replacementBeans.createReplacementProxy(bean, beanName)) {
            // System.out.println("Creating proxy for " + result.getClass().getName());
            return Enhancer.create(clazz, (MethodInterceptor) (obj, method, args, proxy) -> {
                        Object instance = replacementBeans.getService(clazz, bean);
                        // System.out.println("invoking method " + method.getName() + " on " + instance.getClass().getName());
                        return method.invoke(instance, args);
                    }
            );
        }
        return bean;
    }

}

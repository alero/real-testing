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

package org.hrodberaht.injection.plugin.junit.spring.beans;

import org.hrodberaht.injection.core.InjectContainer;
import org.hrodberaht.injection.core.internal.annotation.ReflectionUtils;
import org.hrodberaht.injection.plugin.junit.spring.injector.SpringInject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpringInjectionBeanInjector implements BeanPostProcessor {

    private Map<String, Object> postProcessBeforeInitBeans = new ConcurrentHashMap<>();

    public SpringInjectionBeanInjector() {
        postProcessBeforeInitBeans.put("replacementBeans", new Object());
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!postProcessBeforeInitBeans.containsKey(beanName)
                && containsSpringInjectionAnnotation(bean)) {
            // System.out.println("ADDED bean: " + bean.getClass().getName());
            postProcessBeforeInitBeans.put(beanName, bean);
        }
        // System.out.println("SKIPPED bean: " + bean.getClass().getName());
        return bean;
    }

    private boolean containsSpringInjectionAnnotation(Object bean) {

        List<Member> members = ReflectionUtils.findMembers(bean.getClass());
        for (Member member : members) {
            if (member instanceof Field) {
                Field field = (Field) member;
                SpringInject annotation = field.getAnnotation(SpringInject.class);
                if (annotation != null) {
                    return true;
                }
            }
            if (member instanceof Method) {
                Method method = (Method) member;
                SpringInject annotation = method.getAnnotation(SpringInject.class);
                if (annotation != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public void autowireInjection(Object springBean, String springBeanName, InjectContainer injectContainer) {
        if (springBeanName != null) {
            if (postProcessBeforeInitBeans.containsKey(springBeanName)) {
                injectContainer.injectDependencies(springBean);
            }
        }
    }
}

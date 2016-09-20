package org.hrodberaht.injection.extensions.spring.junit;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.stereotype.Component;

import static org.springframework.cglib.core.TypeUtils.isFinal;

/**
 * Created by robertalexandersson on 4/14/16.
 */
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

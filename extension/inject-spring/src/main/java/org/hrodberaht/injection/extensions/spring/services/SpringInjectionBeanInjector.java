package org.hrodberaht.injection.extensions.spring.services;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.spring.instance.SpringInject;
import org.hrodberaht.injection.internal.annotation.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by alexbrob on 2016-04-01.
 */
public class SpringInjectionBeanInjector implements BeanPostProcessor {

    private Map<String, Object> postProcessBeforeInitBeans = new ConcurrentHashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(!postProcessBeforeInitBeans.containsKey(beanName)
                && containsSpringInjectionAnnotation(bean)) {
            postProcessBeforeInitBeans.put(beanName, bean);
        }
        return bean;
    }

    private boolean containsSpringInjectionAnnotation(Object bean) {

        List<Member> members = ReflectionUtils.findMembers(bean.getClass());
        for(Member member:members){
            if(member instanceof Field) {
                Field field = (Field) member;
                SpringInject annotation = field.getAnnotation(SpringInject.class);
                if(annotation != null){
                    return true;
                }
            }if(member instanceof Method) {
                Method method = (Method) member;
                SpringInject annotation = method.getAnnotation(SpringInject.class);
                if(annotation != null){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public void autowireInjection(Object springBean, String springBeanName, InjectContainer injectContainer) {
           if(postProcessBeforeInitBeans.containsKey(springBeanName)){
               injectContainer.injectDependencies(springBean);
           }
    }
}

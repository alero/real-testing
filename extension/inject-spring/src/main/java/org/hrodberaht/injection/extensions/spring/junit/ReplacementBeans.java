package org.hrodberaht.injection.extensions.spring.junit;

import org.hrodberaht.injection.InjectContainer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Created by robertalexandersson on 4/14/16.
 */
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

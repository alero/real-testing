package org.hrodberaht.injection.extensions.spring.junit;

import org.hrodberaht.injection.extensions.spring.services.ApplicationContextService;
import org.hrodberaht.injection.register.RegistrationModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

/**
 * Created by robertalexandersson on 4/14/16.
 */
public class ContainerLifeCycleTestUtil extends org.hrodberaht.injection.extensions.junit.util.ContainerLifeCycleTestUtil {

    @Autowired
    private ReplacementBeans replacementBeans;

    @Autowired
    private ApplicationContextService applicationContextService;

    @Override
    public void registerServiceInstance(Class serviceDefinition, Object service) {
        if (isSpringBean(serviceDefinition)) {
            replacementBeans.register(serviceDefinition, service);
        }
        super.registerServiceInstance(serviceDefinition, service);
    }

    @Override
    public void registerModule(RegistrationModule module) {
        super.registerModule(module);
    }

    @Override
    public <T> T getService(Class<T> aClass) {
        return super.getService(aClass);
    }

    private boolean isSpringBean(Class clazz) {
        return contains(
                clazz.getAnnotation(Component.class),
                clazz.getAnnotation(Repository.class),
                clazz.getAnnotation(Controller.class),
                clazz.getAnnotation(Service.class));

    }

    private boolean contains(Annotation... annotations) {
        return Stream.of(annotations).anyMatch(annotation -> annotation != null);
    }

    public void reloadSpring() {
        // applicationContextService.refresh();
    }
}

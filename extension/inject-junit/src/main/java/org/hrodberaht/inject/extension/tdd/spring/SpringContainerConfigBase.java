package org.hrodberaht.inject.extension.tdd.spring;

import org.hrodberaht.inject.extension.tdd.ContainerConfigBase;
import org.hrodberaht.inject.extension.tdd.internal.InjectionRegisterScanBase;
import org.hrodberaht.inject.extension.tdd.spring.internal.InjectionRegisterScanSpring;
import org.hrodberaht.inject.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.inject.spi.ThreadConfigHolder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Injection Extension JUnit
 *
 * @author Robert Alexandersson
 *         2010-okt-26 18:59:45
 * @version 1.0
 * @since 1.0
 */
public abstract class SpringContainerConfigBase extends ContainerConfigBase<InjectionRegisterScanSpring> {

    protected SpringContainerConfigBase() {
        DefaultInjectionPointFinder finder = new DefaultInjectionPointFinder() {
            @Override
            protected boolean hasInjectAnnotationOnMethod(Method method) {
                return method.isAnnotationPresent(Autowired.class) ||
                        super.hasInjectAnnotationOnMethod(method);
            }

            @Override
            protected boolean hasInjectAnnotationOnField(Field field) {
                return field.isAnnotationPresent(Autowired.class) ||
                        super.hasInjectAnnotationOnField(field);
            }

            @Override
            protected boolean hasPostConstructAnnotation(Method method) {
                return method.isAnnotationPresent(PostConstruct.class) ||
                        super.hasPostConstructAnnotation(method);
            }

            @Override
            public void extendedInjection(Object service) {
                SpringContainerConfigBase config = (SpringContainerConfigBase) ThreadConfigHolder.get();
                config.injectResources(service);
            }
        };
        // InjectionPointFinder.setInjectionFinder(finder);
    }

    @Override
    protected InjectionRegisterScanBase getScanner() {
        return new InjectionRegisterScanSpring();
    }

    @Override
    protected void addResource(final Class typedName, final Object value) {
        super.addResource(typedName, value);

    }

    @Override
    protected void injectResources(Object serviceInstance) {
        if (resources == null && typedResources == null) {
            return;
        }
        /*
        List<Member> members = ReflectionUtils.findMembers(serviceInstance.getClass());
        for (Member member : members) {
            if (member instanceof Field) {
                Field field = (Field) member;
                if (field.isAnnotationPresent(Autowired.class)) {
                    Qualifier resource = field.getAnnotation(Qualifier.class);
                    if(!injectNamedResource(serviceInstance, field, resource.value())){
                        injectTypedResource(serviceInstance, field);
                    }
                }
            }
        }*/
    }
}

package org.hrodberaht.injection.extensions.junit.ejb;

import org.hrodberaht.injection.extensions.junit.internal.JunitContainerConfigBase;
import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.injection.spi.module.CustomInjectionPointFinderModule;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class EJBContainerConfigUtil {


    public static InjectionRegisterModule createEJBInjectionModule(JunitContainerConfigBase configBase) {
        InjectionRegisterModule injectionRegisterModule = new InjectionRegisterModule();
        injectionRegisterModule.register(new CustomInjectionPointFinderModule(
                new DefaultInjectionPointFinder(configBase) {
                    @Override
                    protected boolean hasInjectAnnotationOnMethod(Method method) {
                        return super.hasInjectAnnotationOnMethod(method);
                    }

                    @Override
                    protected boolean hasInjectAnnotationOnField(Field field) {
                        return field.isAnnotationPresent(EJB.class) ||
                                super.hasInjectAnnotationOnField(field);
                    }

                    @Override
                    protected boolean hasPostConstructAnnotation(Method method) {
                        return method.isAnnotationPresent(PostConstruct.class) ||
                                super.hasPostConstructAnnotation(method);
                    }

                    @Override
                    public Object extendedInjection(Object service) {
                        TDDEJBContainerConfigBase config = (TDDEJBContainerConfigBase) getContainerConfig();
                        config.injectResources(service);
                        return service;
                    }
                }
        ));
        return injectionRegisterModule;
    }


}

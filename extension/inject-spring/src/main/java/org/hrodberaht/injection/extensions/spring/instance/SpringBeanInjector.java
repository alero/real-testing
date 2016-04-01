package org.hrodberaht.injection.extensions.spring.instance;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.spring.services.SpringInjectionBeanInjector;
import org.hrodberaht.injection.internal.InjectionKey;
import org.hrodberaht.injection.internal.annotation.InjectionMetaData;
import org.hrodberaht.injection.internal.annotation.InjectionPoint;
import org.hrodberaht.injection.internal.annotation.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by alexbrob on 2016-04-01.
 */
public class SpringBeanInjector {

    private ApplicationContext context;
    private SpringInjectionBeanInjector springInjectionBeanInjector;
    private Map<InjectionKey, InjectionMetaData> dataMap = new ConcurrentHashMap<>();

    public SpringBeanInjector(ApplicationContext context) {
        this.context = context;
        this.springInjectionBeanInjector = context.getBean(SpringInjectionBeanInjector.class);
    }


    public void inject(Object serviceObject, InjectContainer injectContainer){
        Class serviceClass = serviceObject.getClass();
        // Inject spring bean members

        InjectionKey injectionKey = new InjectionKey(serviceObject.getClass(), false);
        InjectionMetaData injectionMetaData = dataMap.get(injectionKey);
        if(injectionMetaData == null){
            injectionMetaData = createMetaDataAndInject(injectionKey, serviceObject, serviceClass);
            dataMap.put(injectionKey, injectionMetaData);
        }

        for(InjectionPoint injectionPoint:injectionMetaData.getInjectionPoints()){
            SpringInjectionPoint springInjectionPoint = (SpringInjectionPoint)injectionPoint;
            Object springBean = getBean(springInjectionPoint);
            springInjectionBeanInjector.autowireInjection(springBean, springInjectionPoint.getName(), injectContainer);
            if(springInjectionPoint.getType() == InjectionPoint.InjectionPointType.FIELD){
                springInjectionPoint.injectField(serviceObject, springBean);
            }
            if(springInjectionPoint.getType() == InjectionPoint.InjectionPointType.METHOD){
                springInjectionPoint.injectMethod(serviceObject, springBean);
            }
        }
    }

    private Object getBean(SpringInjectionPoint springInjectionPoint) {
        if(springInjectionPoint.getInterfaceClass() != null){
            return context.getBean(springInjectionPoint.getInterfaceClass());
        }
        return context.getBean(springInjectionPoint.getName());
    }

    private InjectionMetaData createMetaDataAndInject(InjectionKey injectionKey, Object serviceObject, Class serviceClass) {
        InjectionMetaData injectionMetaData = new InjectionMetaData(serviceClass, injectionKey, null);
        List<Member> members = ReflectionUtils.findMembers(serviceClass);
        for(Member member:members){
            if(member instanceof Field){
                Field field = (Field) member;
                Qualifier qualifier = field.getAnnotation(Qualifier.class);
                if(qualifier == null){
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    if(autowired != null){
                        Class type = field.getType();
                        Component component = (Component) type.getAnnotation(Component.class);
                        if(component != null){
                            if(component.value().isEmpty()) {
                                String beanName = type.getSimpleName().substring(0, 1).toLowerCase() + type.getSimpleName().substring(1);
                                createFieldInjectionPointAndAddToMetaData(field, beanName, injectionMetaData);
                            }else{
                                String beanName = component.value();
                                createFieldInjectionPointAndAddToMetaData(field, beanName, injectionMetaData);
                            }
                        }else if(type.isInterface()){
                            createFieldInjectionPointAndAddToMetaData(field, type, injectionMetaData);
                        }
                    }
                }else{
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    if(autowired != null){

                    }
                }
            }else if(member instanceof Method){
                Method method = (Method) member;
                Type[] types = method.getGenericParameterTypes();
                for(Type type:types) {
                    if(type instanceof Class) {
                        Class typeClass = (Class)type;
                        Component component = (Component) typeClass.getAnnotation(Component.class);
                        if (component != null) {
                            if (component.value().isEmpty()) {
                                String beanName = typeClass.getSimpleName().substring(0, 1).toLowerCase() + typeClass.getSimpleName().substring(1);
                                createMethodInjectionPointAndAddToMetaData(method, beanName, injectionMetaData);
                            } else {
                                String beanName = component.value();
                                createMethodInjectionPointAndAddToMetaData(method, beanName, injectionMetaData);
                            }
                        }
                    }
                }
            }
        }
        return injectionMetaData;
    }

    private void createFieldInjectionPointAndAddToMetaData(Field field, Class interfaceClass, InjectionMetaData injectionMetaData) {
        SpringInjectionPoint injectionPoint = new SpringInjectionPoint(field, interfaceClass);
        injectionMetaData.getInjectionPoints().add(injectionPoint);
    }

    private void createFieldInjectionPointAndAddToMetaData(Field field, String beanName, InjectionMetaData injectionMetaData) {
        SpringInjectionPoint injectionPoint = new SpringInjectionPoint(field, beanName);
        injectionMetaData.getInjectionPoints().add(injectionPoint);
    }

    private void createMethodInjectionPointAndAddToMetaData(Method method, String beanName, InjectionMetaData injectionMetaData) {
        SpringInjectionPoint injectionPoint = new SpringInjectionPoint(method, beanName);
        injectionMetaData.getInjectionPoints().add(injectionPoint);
    }
}

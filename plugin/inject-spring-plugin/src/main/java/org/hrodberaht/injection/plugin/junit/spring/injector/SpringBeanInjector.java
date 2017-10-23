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

package org.hrodberaht.injection.plugin.junit.spring.injector;

import org.hrodberaht.injection.core.InjectContainer;
import org.hrodberaht.injection.core.internal.InjectionKey;
import org.hrodberaht.injection.core.internal.annotation.InjectionMetaData;
import org.hrodberaht.injection.core.internal.annotation.InjectionPoint;
import org.hrodberaht.injection.core.internal.annotation.ReflectionUtils;
import org.hrodberaht.injection.plugin.junit.spring.beans.SpringInjectionBeanInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpringBeanInjector {

    private static final Logger LOG = LoggerFactory.getLogger(SpringBeanInjector.class);

    private ApplicationContext context;
    private SpringInjectionBeanInjector springInjectionBeanInjector;
    private Map<InjectionKey, InjectionMetaData> dataMap = new ConcurrentHashMap<>();

    public SpringBeanInjector(ApplicationContext context) {
        this.context = context;
        this.springInjectionBeanInjector = context.getBean(SpringInjectionBeanInjector.class);
    }


    public void inject(Object serviceObject, InjectContainer injectContainer) {
        Class serviceClass = serviceObject.getClass();
        LOG.info("Spring injection for {}", serviceClass);
        // Inject spring bean members

        InjectionKey injectionKey = new InjectionKey(serviceObject.getClass(), false);
        InjectionMetaData injectionMetaData = dataMap.get(injectionKey);
        if (injectionMetaData == null) {
            injectionMetaData = createMetaDataAndInject(injectionKey, serviceObject, serviceClass);
            dataMap.put(injectionKey, injectionMetaData);
        }

        for (InjectionPoint injectionPoint : injectionMetaData.getInjectionPoints()) {
            SpringInjectionPoint springInjectionPoint = (SpringInjectionPoint) injectionPoint;
            try {
                Object springBean = getBean(springInjectionPoint);
                springInjectionBeanInjector.autowireInjection(springBean,
                        springInjectionPoint.getName(), injectContainer);
                if (springInjectionPoint.getType() == InjectionPoint.InjectionPointType.FIELD) {
                    springInjectionPoint.injectField(serviceObject, springBean);
                } else if (springInjectionPoint.getType() == InjectionPoint.InjectionPointType.METHOD) {
                    springInjectionPoint.injectMethod(serviceObject, springBean);
                }
            } catch (NoSuchBeanDefinitionException e) {
                // TODO: why do we need to do it like this? should there be a switch to enable "errors" as default?
                String message = "No bean found of type: " + springInjectionPoint.getDisplayName() + " for service: " + serviceClass.getName();
                LOG.warn(message);
                // throw new RuntimeException(message, e);
            }
        }
    }

    private Object getBean(SpringInjectionPoint springInjectionPoint) {
        if (springInjectionPoint.getInterfaceClass() != null) {
            return context.getBean(springInjectionPoint.getInterfaceClass());
        }
        return context.getBean(springInjectionPoint.getName());
    }

    private InjectionMetaData createMetaDataAndInject(InjectionKey injectionKey, Object serviceObject, Class serviceClass) {
        InjectionMetaData injectionMetaData = new InjectionMetaData(serviceClass, injectionKey, null);
        List<Member> members = ReflectionUtils.findMembers(serviceClass);
        for (Member member : members) {
            if (member instanceof Field) {
                Field field = (Field) member;
                Qualifier qualifier = field.getAnnotation(Qualifier.class);
                if (qualifier == null) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    if (autowired != null) {
                        Class type = field.getType();
                        Annotation stereotype = getStereotype(type);
                        if (stereotype != null) {
                            String stereotypeValue = getStereotypeValue(type);
                            if (stereotypeValue.isEmpty()) {
                                String beanName = type.getSimpleName().substring(0, 1).toLowerCase() + type.getSimpleName().substring(1);
                                createFieldInjectionPointAndAddToMetaData(field, beanName, injectionMetaData);
                            } else {
                                String beanName = stereotypeValue;
                                createFieldInjectionPointAndAddToMetaData(field, beanName, injectionMetaData);
                            }
                        } else if (type.isInterface()) {
                            createFieldInjectionPointAndAddToMetaData(field, type, injectionMetaData);
                        } else if (!type.isAnonymousClass() && !type.isMemberClass()) {
                            createFieldInjectionPointAndAddToMetaData(field, type, injectionMetaData);
                        }
                    }
                } else {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    if (autowired != null) {
                        // Class type = field.getType();
                        // Annotation stereotype = getStereotype(type);

                        String beanName = qualifier.value();
                        createFieldInjectionPointAndAddToMetaData(field, beanName, injectionMetaData);

                    }
                }
            } else if (member instanceof Method) {
                Method method = (Method) member;
                Type[] types = method.getGenericParameterTypes();
                for (Type type : types) {
                    if (type instanceof Class) {
                        Class typeClass = (Class) type;
                        Annotation stereoType = getStereotype(typeClass);
                        if (stereoType != null) {
                            String stereoTypeValue = getStereotypeValue(typeClass);
                            if (stereoTypeValue.isEmpty()) {
                                String beanName = typeClass.getSimpleName().substring(0, 1).toLowerCase() + typeClass.getSimpleName().substring(1);
                                createMethodInjectionPointAndAddToMetaData(method, beanName, injectionMetaData);
                            } else {
                                String beanName = stereoTypeValue;
                                createMethodInjectionPointAndAddToMetaData(method, beanName, injectionMetaData);
                            }
                        }
                    }
                }
            }
        }
        return injectionMetaData;
    }

    private String getStereotypeValue(Class type) {
        Annotation stereotype = type.getAnnotation(Component.class);
        if (stereotype != null) {
            return ((Component) stereotype).value();
        }
        stereotype = type.getAnnotation(Service.class);
        if (stereotype != null) {
            return ((Service) stereotype).value();
        }
        stereotype = type.getAnnotation(Controller.class);
        if (stereotype != null) {
            return ((Controller) stereotype).value();
        }
        stereotype = type.getAnnotation(Repository.class);
        if (stereotype != null) {
            return ((Repository) stereotype).value();
        }
        return "";
    }

    private Annotation getStereotype(Class type) {
        Annotation stereotype = type.getAnnotation(Component.class);
        if (stereotype == null) {
            stereotype = type.getAnnotation(Repository.class);
        }
        if (stereotype == null) {
            stereotype = type.getAnnotation(Service.class);
        }
        if (stereotype == null) {
            stereotype = type.getAnnotation(Controller.class);
        }
        return stereotype;
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

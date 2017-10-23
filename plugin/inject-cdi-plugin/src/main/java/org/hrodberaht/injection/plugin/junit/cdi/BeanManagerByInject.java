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

package org.hrodberaht.injection.plugin.junit.cdi;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMember;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanAttributes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Decorator;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.InjectionTargetFactory;
import javax.enterprise.inject.spi.InterceptionFactory;
import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.inject.spi.Interceptor;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.inject.spi.ProducerFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

public class BeanManagerByInject implements BeanManager {
    public Object getReference(Bean<?> bean, Type beanType, CreationalContext<?> ctx) {
        return null;
    }

    public Object getInjectableReference(InjectionPoint ij, CreationalContext<?> ctx) {
        return null;
    }

    public <T> CreationalContext<T> createCreationalContext(Contextual<T> contextual) {
        return null;
    }

    public Set<Bean<?>> getBeans(Type beanType, Annotation... qualifiers) {
        return null;
    }

    public Set<Bean<?>> getBeans(String name) {
        return null;
    }

    public Bean<?> getPassivationCapableBean(String id) {
        return null;
    }

    public <X> Bean<? extends X> resolve(Set<Bean<? extends X>> beans) {
        return null;
    }

    public void validate(InjectionPoint injectionPoint) {
    }

    public void fireEvent(Object event, Annotation... qualifiers) {

    }

    public <T> Set<ObserverMethod<? super T>> resolveObserverMethods(T event, Annotation... qualifiers) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Decorator<?>> resolveDecorators(Set<Type> types, Annotation... qualifiers) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Interceptor<?>> resolveInterceptors(InterceptionType type, Annotation... interceptorBindings) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isScope(Class<? extends Annotation> annotationType) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isNormalScope(Class<? extends Annotation> annotationType) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isPassivatingScope(Class<? extends Annotation> annotationType) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isQualifier(Class<? extends Annotation> annotationType) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isInterceptorBinding(Class<? extends Annotation> annotationType) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isStereotype(Class<? extends Annotation> annotationType) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<Annotation> getInterceptorBindingDefinition(Class<? extends Annotation> bindingType) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<Annotation> getStereotypeDefinition(Class<? extends Annotation> stereotype) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean areQualifiersEquivalent(Annotation annotation, Annotation annotation1) {
        return false;
    }

    @Override
    public boolean areInterceptorBindingsEquivalent(Annotation annotation, Annotation annotation1) {
        return false;
    }

    @Override
    public int getQualifierHashCode(Annotation annotation) {
        return 0;
    }

    @Override
    public int getInterceptorBindingHashCode(Annotation annotation) {
        return 0;
    }

    public Context getContext(Class<? extends Annotation> scopeType) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public javax.el.ELResolver getELResolver() {
        return null;
    }

    public javax.el.ExpressionFactory wrapExpressionFactory(javax.el.ExpressionFactory expressionFactory) {
        return null;
    }

    public <T> AnnotatedType<T> createAnnotatedType(Class<T> type) {
        return null;
    }

    public <T> InjectionTarget<T> createInjectionTarget(AnnotatedType<T> type) {
        return null;
    }

    @Override
    public <T> InjectionTargetFactory<T> getInjectionTargetFactory(AnnotatedType<T> annotatedType) {
        return null;
    }

    @Override
    public <X> ProducerFactory<X> getProducerFactory(AnnotatedField<? super X> annotatedField, Bean<X> bean) {
        return null;
    }

    @Override
    public <X> ProducerFactory<X> getProducerFactory(AnnotatedMethod<? super X> annotatedMethod, Bean<X> bean) {
        return null;
    }

    @Override
    public <T> BeanAttributes<T> createBeanAttributes(AnnotatedType<T> annotatedType) {
        return null;
    }

    @Override
    public BeanAttributes<?> createBeanAttributes(AnnotatedMember<?> annotatedMember) {
        return null;
    }

    @Override
    public <T> Bean<T> createBean(BeanAttributes<T> beanAttributes, Class<T> aClass, InjectionTargetFactory<T> injectionTargetFactory) {
        return null;
    }

    @Override
    public <T, X> Bean<T> createBean(BeanAttributes<T> beanAttributes, Class<X> aClass, ProducerFactory<X> producerFactory) {
        return null;
    }

    @Override
    public InjectionPoint createInjectionPoint(AnnotatedField<?> annotatedField) {
        return null;
    }

    @Override
    public InjectionPoint createInjectionPoint(AnnotatedParameter<?> annotatedParameter) {
        return null;
    }

    @Override
    public <T extends Extension> T getExtension(Class<T> aClass) {
        return null;
    }

    @Override
    public <T> InterceptionFactory<T> createInterceptionFactory(CreationalContext<T> creationalContext, Class<T> aClass) {
        return null;
    }

    @Override
    public Event<Object> getEvent() {
        return null;
    }

    @Override
    public Instance<Object> createInstance() {
        return null;
    }
}

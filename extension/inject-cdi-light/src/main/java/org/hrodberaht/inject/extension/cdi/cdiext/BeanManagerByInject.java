package org.hrodberaht.inject.extension.cdi.cdiext;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Decorator;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.inject.spi.Interceptor;
import javax.enterprise.inject.spi.ObserverMethod;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2014-03-03
 * Time: 05:57
 * To change this template use File | Settings | File Templates.
 */
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
}

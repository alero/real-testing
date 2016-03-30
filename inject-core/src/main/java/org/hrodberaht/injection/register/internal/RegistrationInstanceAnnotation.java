package org.hrodberaht.injection.register.internal;

import org.hrodberaht.injection.internal.InjectionContainerManager;
import org.hrodberaht.injection.internal.InjectionKey;
import org.hrodberaht.injection.internal.ScopeContainer;
import org.hrodberaht.injection.register.InjectionFactory;
import org.hrodberaht.injection.register.VariableInjectionFactory;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-jun-03 17:53:13
 * @version 1.0
 * @since 1.0
 */
public class RegistrationInstanceAnnotation<T extends Registration> implements Registration {

    protected Class theInterface;

    // Producers
    protected Class theService;
    protected Object theInstance;
    private InjectionFactory theFactory;
    private VariableInjectionFactory theVariableFactory;

    protected String name;
    protected Class<? extends Annotation> annotation;
    protected InjectionContainerManager.Scope scope = null; // No default scope for registration


    public RegistrationInstanceAnnotation(Class theInterface) {
        this.theInterface = theInterface;
        this.theService = theInterface;
    }

    @SuppressWarnings(value = "unchecked")
    public T annotated(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
        return (T) this;
    }

    @SuppressWarnings(value = "unchecked")
    public T named(String named) {
        this.name = named;
        return (T) this;
    }

    public T with(Class theService) {
        this.theService = theService;
        return (T) this;
    }

    public T withInstance(Object theInstance) {
        this.theInstance = theInstance;
        this.theService = theInstance.getClass();
        this.scope = ScopeContainer.Scope.SINGLETON;
        return (T) this;
    }

    public T withFactory(InjectionFactory aFactory) {
        this.theFactory = aFactory;
        this.theService = theFactory.getInstanceType();
        this.scope = ScopeContainer.Scope.NEW;
        return (T) this;
    }

    public T withVariableFactory(VariableInjectionFactory variableInjectionFactory) {
        this.theVariableFactory = variableInjectionFactory;
        this.name = VariableInjectionFactory.SERVICE_NAME;
        this.scope = ScopeContainer.Scope.NEW;
        return (T) this;
    }


    public Class getService() {
        return theService;
    }

    public Object getTheInstance() {
        return theInstance;
    }

    public InjectionFactory getTheFactory() {
        return theFactory;
    }

    public VariableInjectionFactory getTheVariableFactory() {
        return theVariableFactory;
    }

    public T scopeAs(ScopeContainer.Scope scope) {
        this.scope = scope;
        return (T) this;
    }

    public ScopeContainer.Scope getScope() {
        return scope;
    }


    public InjectionKey getInjectionKey() {
        if (annotation != null) {
            return new InjectionKey(annotation, theInterface, false);
        } else if (name != null) {
            return new InjectionKey(name, theInterface, false);
        }
        return new InjectionKey(theInterface, false);
    }


}
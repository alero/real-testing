package org.hrodberaht.injection.register;

public class SimpleInjectionFactory<T> implements InjectionFactory<T> {

    private T instance;

    public SimpleInjectionFactory(T instance) {
        this.instance = instance;
    }

    @Override
    public T getInstance() {
        return instance;
    }

    @Override
    public Class<T> getInstanceType() {
        return (Class<T>) instance.getClass();
    }

    @Override
    public boolean newObjectOnInstance() {
        return false;
    }
}

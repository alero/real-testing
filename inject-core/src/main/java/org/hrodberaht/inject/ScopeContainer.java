package org.hrodberaht.inject;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-jun-06 02:06:55
 * @version 1.0
 * @since 1.0
 */
public interface ScopeContainer extends InjectContainer {

    public enum Scope {
        SINGLETON, NEW, THREAD, INHERITABLE_THREAD
    }

    /**
     * Will force the registered service to have scope NEW when created. No matter how it was registered.
     *
     * @param service
     * @param <T>
     * @return a service that is scoped as NEW,
     * sequential retrievals of the same service with forced new will give the different instances back.
     */
    <T> T getNew(Class<T> service);

    /**
     * Will force the registered service to have scope SINGLETON when created. No matter how it was registered.
     *
     * @param service
     * @param <T>
     * @return a service that is scoped as SINGLETON,
     * sequential retrievals of the same service with forced singleton will give the same instance back.
     */
    <T> T getSingleton(Class<T> service);
}

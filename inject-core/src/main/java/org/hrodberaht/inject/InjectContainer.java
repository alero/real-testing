package org.hrodberaht.inject;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-jun-06 02:06:55
 * @version 1.0
 * @since 1.0
 */
public interface InjectContainer extends Container {

    public void injectDependencies(Object service);

    <T, K> T get(Class<T> service, K variable);
}

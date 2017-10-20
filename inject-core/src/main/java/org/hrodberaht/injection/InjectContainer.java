package org.hrodberaht.injection;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-jun-06 02:06:55
 * @version 1.0
 * @since 1.0
 */
public interface InjectContainer {


    /**
     * Will inject only inner container resources (no extended injections and no post-construct)
     *
     * @param service
     */
    void injectDependencies(Object service);

    /**
     * Will inject all dependencies (inner and extended) as well as post-construct
     *
     * @param service
     */
    void autowireAndPostConstruct(Object service);

    <T, K> T get(Class<T> service, K variable);


    /**
     * @param service
     * @param <T>
     * @return an instance of the service class with qualifier
     */
    <T> T get(Class<T> service);

    /**
     * @param service
     * @param qualifier
     * @param <T>
     * @return an instance of the service class with qualifier
     */
    <T> T get(Class<T> service, String qualifier);

    <T> T get(Class<T> service, Class<? extends Annotation> qualifier);


}

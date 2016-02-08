package org.hrodberaht.inject;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-jun-06 02:00:16
 * @version 1.0
 * @since 1.0
 */
public interface Container {

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

package org.hrodberaht.injection.spi;

public interface JavaResourceCreator<T> {
    /**
     * Will create a resource and register it to the jndi space
     * @param name the name of the resource in the jndi space
     * @return the created object
     */
    T create(String name);

    /**
     * This will NOT register to JNDI space and the resource must be managed by the creator
     * @return the created object
     */
    T create();

    /**
     * This will NOT register to JNDI space and the resource must be managed by the creator
     * @return the created object
     */
    T create(String name, T instance);

    /**
     *
     * @param instance
     * @return
     */
    T create(T instance);

}

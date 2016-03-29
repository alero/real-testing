package org.hrodberaht.injection.register;

/**
 * Created by IntelliJ IDEA.
 * User: Robert Work
 * Date: 2010-aug-18
 * Time: 15:25:44
 * To change this template use File | Settings | File Templates.
 */
public interface InjectionFactory<T> {
    T getInstance();

    Class getInstanceType();

    boolean newObjectOnInstance();
}

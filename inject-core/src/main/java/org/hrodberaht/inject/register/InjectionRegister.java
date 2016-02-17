package org.hrodberaht.inject.register;

import org.hrodberaht.inject.Container;
import org.hrodberaht.inject.InjectionContainerManager;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-aug-01 16:43:03
 * @version 1.0
 * @since 1.0
 */
public interface InjectionRegister {

    InjectionRegister register(Class<? extends Annotation> qualifier, Class serviceDefinition, Class service);

    InjectionRegister register(Class serviceDefinition, Class service);

    InjectionRegister register(Class service);

    InjectionRegister overrideRegister(Class<? extends Annotation> qualifier, Class serviceDefinition, Class service);

    InjectionRegister overrideRegister(Class serviceDefinition, Class service);

    InjectionRegister overrideRegister(Class service);

    InjectionRegister finalRegister(Class<? extends Annotation> qualifier, Class serviceDefinition, Class service);

    InjectionRegister finalRegister(Class serviceDefinition, Class service);

    InjectionRegister finalRegister(Class service);

    InjectionContainerManager getInnerContainer();

    Container getContainer();
}

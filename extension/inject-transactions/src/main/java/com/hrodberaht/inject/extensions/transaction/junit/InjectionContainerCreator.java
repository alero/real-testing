package com.hrodberaht.inject.extensions.transaction.junit;

import org.hrodberaht.injection.InjectContainer;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-12 19:45:07
 * @version 1.0
 * @since 1.0
 */
public interface InjectionContainerCreator {
    InjectContainer createContainer();
}

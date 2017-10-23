/*
 * Copyright (c) 2017 org.hrodberaht
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hrodberaht.injection.core.register.internal;

import org.hrodberaht.injection.core.register.InjectionFactory;
import org.hrodberaht.injection.core.register.VariableInjectionFactory;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * 2010-jun-17 20:22:48
 * @version 1.0
 * @since 1.0
 */
public interface Registration {
    Registration annotated(Class<? extends Annotation> annotation);

    Registration named(String named);

    /**
     * Register the class and follow the scope annotations of the class,
     * injects deeper dependencies via the container
     *
     * @param theService
     * @return the builder
     */
    Registration with(Class theService);

    /**
     * Register the instance as singleton,
     * injects deeper dependencies via the container
     *
     * @param aSingleton the instance to register as singleton
     * @return the builder
     */
    Registration withInstance(Object aSingleton);

    /**
     * Register the factory as the instance provider (internally no singleton is created),
     * injects nothing deeper into the service (lifecycle is managed via the factory)
     *
     * @param aFactory a manually created factory
     * @return the builder
     */
    Registration withFactory(InjectionFactory aFactory);

    /**
     * Register the instance via a simple factory as the instance provider, works similar to singleton
     * injects nothing deeper into the service (lifecycle is managed via the simple factory factory)
     * For more info on the factory used {@link org.hrodberaht.injection.core.register.SimpleInjectionFactory}
     *
     * @param aSingleton the instance
     * @return the builder
     */
    Registration withFactoryInstance(Object aSingleton);

    /**
     * Register the variable factory as the instance provider (internally no singleton is created),
     * injects nothing deeper into the service (lifecycle is managed via the factory)
     * <p>
     * Using a variable provider factory means using the custom annotation
     * {@link org.hrodberaht.injection.core.annotation.VariableProvider}
     *
     * @param variableInjectionFactory a manually created variable factory
     * @return the builder
     */
    Registration withVariableFactory(VariableInjectionFactory variableInjectionFactory);
}

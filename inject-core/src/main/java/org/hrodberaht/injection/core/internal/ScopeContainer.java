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

package org.hrodberaht.injection.core.internal;

import org.hrodberaht.injection.core.InjectContainer;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-jun-06 02:06:55
 * @version 1.0
 * @since 1.0
 */
public interface ScopeContainer extends InjectContainer {

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

    public enum Scope {
        SINGLETON, NEW, THREAD, INHERITABLE_THREAD
    }
}

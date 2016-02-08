/*
 * ~ Copyright (c) 2010.
 *   ~ Licensed under the Apache License, Version 2.0 (the "License");
 *   ~ you may not use this file except in compliance with the License.
 *   ~ You may obtain a copy of the License at
 *   ~
 *   ~        http://www.apache.org/licenses/LICENSE-2.0
 *   ~
 *   ~ Unless required by applicable law or agreed to in writing, software
 *   ~ distributed under the License is distributed on an "AS IS" BASIS,
 *   ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   ~ See the License for the specific language governing permissions and limitations under the License.
 */

package org.hrodberaht.inject.internal;

import org.hrodberaht.inject.SimpleInjection;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-29 12:48:43
 * @version 1.0
 * @since 1.0
 */
public interface InjectionContainer {

    <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, String qualifier);

    <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, Class<? extends Annotation> qualifier);

    <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope);

    <T, K> T getService(Class<T> service, K variable);

    Collection<ServiceRegister> getServiceRegister();

    Object clone(SimpleInjection simpleInjection) throws CloneNotSupportedException;

}

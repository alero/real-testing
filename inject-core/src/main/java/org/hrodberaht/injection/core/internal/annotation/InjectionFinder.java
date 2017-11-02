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

package org.hrodberaht.injection.core.internal.annotation;

import org.hrodberaht.injection.core.internal.InjectionKey;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Injection Extension JUnit
 *
 * @author Robert Alexandersson
 * 2010-sep-23 20:33:47
 * @version 1.0
 * @since 1.0
 */
public interface InjectionFinder {

    enum InjectionType {EXTENDED, OVERRIDDEN}

    List<InjectionPoint> findInjectionPoints(Class instanceClass, AnnotationInjection annotationInjection);

    Method findPostConstruct(Class instanceClass);

    Object extendedInjection(Object instance);

    Object extendedInjection(InjectionKey instanceClass);

    default InjectionType getInjectionType() {
        return InjectionType.EXTENDED;
    }


}

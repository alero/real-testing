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

package org.hrodberaht.inject.testservices.regmodules;

import org.hrodberaht.inject.annotation.ExtendedResourceInjection;
import org.hrodberaht.inject.testservices.annotated.Injected;
import org.hrodberaht.inject.testservices.annotated.PostConstructInit;
import org.hrodberaht.injection.core.internal.annotation.DefaultInjectionPointFinder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2013-10-08
 * Time: 10:58
 * To change this template use File | Settings | File Templates.
 */
public class CustomInjectionPointFinder extends DefaultInjectionPointFinder {
    @Override
    protected boolean hasInjectAnnotationOnMethod(Method method) {
        return method.isAnnotationPresent(Injected.class) ||
                super.hasInjectAnnotationOnMethod(method);
    }

    @Override
    protected boolean hasInjectAnnotationOnField(Field field) {
        return field.isAnnotationPresent(Injected.class) ||
                super.hasInjectAnnotationOnField(field);
    }

    @Override
    protected boolean hasPostConstructAnnotation(Method method) {
        return method.isAnnotationPresent(PostConstructInit.class) ||
                super.hasPostConstructAnnotation(method);
    }

    @Override
    public Object extendedInjection(Object service) {
        ExtendedResourceInjection.injectText(service, "Text");
        return service;
    }
}

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

package org.hrodberaht.injection.plugin.junit.cdi;

import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator;
import java.lang.annotation.Annotation;


public class BeforeBeanDiscoveryByInject implements BeforeBeanDiscovery {


    public void addQualifier(Class<? extends Annotation> aClass) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addQualifier(AnnotatedType<? extends Annotation> annotatedType) {

    }

    public void addScope(Class<? extends Annotation> aClass, boolean b, boolean b1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addStereotype(Class<? extends Annotation> aClass, Annotation... annotations) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addInterceptorBinding(AnnotatedType<? extends Annotation> annotatedType) {

    }

    @Override
    public void addInterceptorBinding(Class<? extends Annotation> aClass, Annotation... annotations) {

    }

    public void addInterceptorBinding(Class<? extends Annotation> aClass) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addAnnotatedType(AnnotatedType<?> annotatedType) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addAnnotatedType(AnnotatedType<?> annotatedType, String s) {

    }

    @Override
    public <T> AnnotatedTypeConfigurator<T> addAnnotatedType(Class<T> aClass, String s) {
        return null;
    }

    @Override
    public <T extends Annotation> AnnotatedTypeConfigurator<T> configureQualifier(Class<T> aClass) {
        return null;
    }

    @Override
    public <T extends Annotation> AnnotatedTypeConfigurator<T> configureInterceptorBinding(Class<T> aClass) {
        return null;
    }
}

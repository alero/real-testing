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

import org.hrodberaht.injection.register.InjectionRegister;

import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.inject.spi.configurator.BeanConfigurator;
import javax.enterprise.inject.spi.configurator.ObserverMethodConfigurator;

public class AfterBeanDiscoveryByInject implements AfterBeanDiscovery {


    public AfterBeanDiscoveryByInject(InjectionRegister injectionRegisterScanInterface) {
    }

    public void addDefinitionError(Throwable throwable) {

    }

    public void addBean(Bean<?> bean) {

    }

    @Override
    public <T> BeanConfigurator<T> addBean() {
        return null;
    }

    public void addObserverMethod(ObserverMethod<?> observerMethod) {

    }

    @Override
    public <T> ObserverMethodConfigurator<T> addObserverMethod() {
        return null;
    }

    public void addContext(Context context) {

    }

    @Override
    public <T> AnnotatedType<T> getAnnotatedType(Class<T> aClass, String s) {
        return null;
    }

    @Override
    public <T> Iterable<AnnotatedType<T>> getAnnotatedTypes(Class<T> aClass) {
        return null;
    }
}

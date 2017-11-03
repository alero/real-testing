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

package org.hrodberaht.injection.extensions.cdi.example.service;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2014-06-26
 * Time: 09:24
 * To change this template use File | Settings | File Templates.
 */
public class CDIExampleExtension implements Extension {

    private static boolean afterBeanDiscoveryInitiated = false;
    private static boolean beforeBeanDiscoveryInitiated = false;

    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {
        beforeBeanDiscoveryInitiated = true;
    }


    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {
    }


    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd) {
        afterBeanDiscoveryInitiated = true;
    }

    public boolean isAfterBeanDiscoveryInitiated() {
        return afterBeanDiscoveryInitiated;
    }

    public boolean isBeforeBeanDiscoveryInitiated() {
        return beforeBeanDiscoveryInitiated;
    }
}

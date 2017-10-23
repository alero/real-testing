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

package org.hrodberaht.injection.plugin.junit.cdi.cdi_ext;


import org.hrodberaht.injection.plugin.junit.cdi.service.ConstantClassLoadedPostContainer;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.inject.Inject;


/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-12-03
 * Time: 12:25
 * To change this template use File | Settings | File Templates.
 */
public class CDIExtension implements Extension {

    @Inject
    public ConstantClassLoadedPostContainer constantClassLoadedPostContainer;

    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {

        System.out.println("beginning the scanning process");

    }


    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {

        System.out.println("scanning type: " + pat.getAnnotatedType().getJavaClass().getName());

    }


    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd) {

        System.out.println("finished the scanning process");
        constantClassLoadedPostContainer.init();

    }


}

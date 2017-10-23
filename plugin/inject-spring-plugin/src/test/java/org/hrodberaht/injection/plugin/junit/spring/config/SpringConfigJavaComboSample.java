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

package org.hrodberaht.injection.plugin.junit.spring.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.plugin.junit.spring.beans.incubator.ReplacementBeans;
import org.hrodberaht.injection.plugin.junit.spring.injector.SpringInject;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.PlatformTransactionManager;

import static org.hrodberaht.injection.plugin.junit.spring.config.SpringConfigJavaComboSample._package;


@Configuration
@ComponentScan(basePackages = _package)
@ImportResource(locations = "/META-INF/spring-config-datasource.xml")
public class SpringConfigJavaComboSample {

    static final String _package = "org.hrodberaht.injection.plugin.junit.spring.testservices.spring";

    @SpringInject
    private InjectContainer injectContainer;

    @Bean(name = "replacementBeans")
    public ReplacementBeans getReplacementBeans() {
        return new ReplacementBeans(injectContainer, _package);
    }


    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return Mockito.mock(PlatformTransactionManager.class);
    }


}

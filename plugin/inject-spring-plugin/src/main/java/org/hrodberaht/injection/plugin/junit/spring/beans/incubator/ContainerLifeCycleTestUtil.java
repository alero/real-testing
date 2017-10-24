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

package org.hrodberaht.injection.plugin.junit.spring.beans.incubator;

import org.hrodberaht.injection.plugin.junit.spring.beans.ApplicationContextService;
import org.hrodberaht.injection.core.register.RegistrationModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.stream.Stream;

public class ContainerLifeCycleTestUtil extends org.hrodberaht.injection.plugin.junit.ContainerLifeCycleTestUtil {

    @Autowired
    private ReplacementBeans replacementBeans;

    @Autowired
    private ApplicationContextService applicationContextService;

    @Override
    public void registerServiceInstance(Class serviceDefinition, Object service) {
        if (isSpringBean(serviceDefinition)) {
            replacementBeans.register(serviceDefinition, service);
        }
        super.registerServiceInstance(serviceDefinition, service);
    }

    private boolean isSpringBean(Class clazz) {
        return contains(
                clazz.getAnnotation(Component.class),
                clazz.getAnnotation(Repository.class),
                clazz.getAnnotation(Controller.class),
                clazz.getAnnotation(Service.class));

    }

    private boolean contains(Annotation... annotations) {
        return Stream.of(annotations).anyMatch(Objects::nonNull);
    }

    public void reloadSpring() {
        // applicationContextService.getApplicationContext().
    }
}

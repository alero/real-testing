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

package org.hrodberaht.injection.plugin.junit.plugins;

import org.hrodberaht.injection.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.injection.plugin.junit.cdi.ApplicationCDIExtensions;
import org.hrodberaht.injection.plugin.junit.cdi.CDIExtensions;
import org.hrodberaht.injection.plugin.junit.spi.InjectionPlugin;
import org.hrodberaht.injection.plugin.junit.spi.RunnerPlugin;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.spi.ContainerConfigBuilder;

import javax.ejb.EJB;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CDIInjectionPlugin implements InjectionPlugin, RunnerPlugin {

    private final CDIExtensions cdiExtensions = new ApplicationCDIExtensions();


    @Override
    public void setInjectionRegister(InjectionRegister injectionRegister) {

    }

    @Override
    public DefaultInjectionPointFinder getInjectionFinder(ContainerConfigBuilder containerConfigBuilder) {
        return new CDIInjectionPointFinder(containerConfigBuilder);
    }

    @Override
    public LifeCycle getLifeCycle() {
        return LifeCycle.TEST_SUITE;
    }


    @Override
    public void beforeContainerCreation() {
        cdiExtensions.runBeforeBeanDiscovery();
    }

    @Override
    public void afterContainerCreation(InjectionRegister injectionRegister) {
        cdiExtensions.runAfterBeanDiscovery(injectionRegister);
    }

    @Override
    public void beforeTest(InjectionRegister injectionRegisterr) {

    }

    @Override
    public void beforeTestClass(InjectionRegister injectionRegister) {

    }

    @Override
    public void afterTestClass(InjectionRegister injectionRegister) {

    }

    @Override
    public void afterTest(InjectionRegister injectionRegister) {

    }


    private static class CDIInjectionPointFinder extends DefaultInjectionPointFinder {
        private boolean annotationForEJB = true;

        private CDIInjectionPointFinder(ContainerConfigBuilder containerConfigBuilder) {
            super(containerConfigBuilder);
        }

        @Override
        protected boolean hasInjectAnnotationOnMethod(Method method) {
            try {

                return (annotationForEJB && method.isAnnotationPresent(EJB.class))
                        || super.hasInjectAnnotationOnMethod(method);
            } catch (NoClassDefFoundError error) {
                annotationForEJB = false;
                return super.hasInjectAnnotationOnMethod(method);
            }
        }

        @Override
        protected boolean hasInjectAnnotationOnField(Field field) {
            try {
                return (annotationForEJB && field.isAnnotationPresent(EJB.class)) ||
                        super.hasInjectAnnotationOnField(field);
            } catch (NoClassDefFoundError error) {
                annotationForEJB = false;
                return super.hasInjectAnnotationOnField(field);
            }
        }

    }
}

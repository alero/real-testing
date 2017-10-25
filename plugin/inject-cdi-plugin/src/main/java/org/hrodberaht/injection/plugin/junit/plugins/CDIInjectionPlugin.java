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

import org.hrodberaht.injection.core.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.injection.core.spi.ContainerConfigBuilder;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.PluginContext;
import org.hrodberaht.injection.plugin.junit.api.annotation.InjectionPluginInjectionFinder;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterContainerCreation;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeContainerCreation;
import org.hrodberaht.injection.plugin.junit.cdi.ApplicationCDIExtensions;
import org.hrodberaht.injection.plugin.junit.cdi.CDIExtensions;

import javax.ejb.EJB;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CDIInjectionPlugin implements Plugin {

    private final CDIExtensions cdiExtensions = new ApplicationCDIExtensions();

    @InjectionPluginInjectionFinder
    protected DefaultInjectionPointFinder getInjectionFinder(ContainerConfigBuilder containerConfigBuilder) {
        return new CDIInjectionPointFinder(containerConfigBuilder);
    }

    @Override
    public LifeCycle getLifeCycle() {
        return LifeCycle.TEST_SUITE;
    }


    @RunnerPluginBeforeContainerCreation
    protected void beforeContainerCreation(PluginContext pluginContext) {
        cdiExtensions.runBeforeBeanDiscovery();
    }

    @RunnerPluginAfterContainerCreation
    protected void afterContainerCreation(PluginContext pluginContext) {
        cdiExtensions.runAfterBeanDiscovery(pluginContext.register());
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

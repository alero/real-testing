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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.hrodberaht.injection.core.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.injection.core.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.core.register.InjectionRegister;
import org.hrodberaht.injection.core.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.core.spi.ContainerConfigBuilder;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.annotation.InjectionPluginInjectionFinder;
import org.hrodberaht.injection.plugin.junit.api.annotation.InjectionPluginInjectionRegister;

import java.util.List;

public class GuicePlugin implements Plugin {

    private Injector injector;

    @InjectionPluginInjectionRegister
    private void setInjectionRegister(InjectionRegister injectionRegister) {
        if (injector != null) {
            injectionRegister.register(new RegistrationModuleAnnotation() {
                @Override
                public void registrations() {
                    register(Injector.class).withFactoryInstance(injector);
                }
            });
        }
    }

    @InjectionPluginInjectionFinder
    private InjectionFinder getInjectionFinder(ContainerConfigBuilder containerConfigBuilder) {
        return new DefaultInjectionPointFinder(containerConfigBuilder) {
            @Override
            public Object extendedInjection(Object instance) {
                // This will rewire the instance to become a "guice-instance"
                instance = injector.getInstance(instance.getClass());
                return super.extendedInjection(instance);
            }
        };
    }

    @Override
    public LifeCycle getLifeCycle() {
        return LifeCycle.TEST_SUITE;
    }


    public GuicePlugin loadModules(Module... modules) {
        injector = Guice.createInjector(modules);
        return this;
    }

    public GuicePlugin loadModules(List<Module> modules) {
        injector = Guice.createInjector(modules);
        return this;
    }
}

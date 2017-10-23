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

import org.hrodberaht.injection.config.ContainerConfig;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;


public class SpringInjectionRegistryBuilder<T extends SpringModule> extends InjectionRegistryBuilder<T> {

    public SpringInjectionRegistryBuilder(ContainerConfig configBase) {
        super(configBase);
    }

    private Class[] springConfigs = null;

    @Override
    protected T createModuleContainer() {
        return (T) new SpringModule(getContainer());
    }

    public SpringInjectionRegistryBuilder springConfig(SpringModuleFunc scanModuleFunc) {
        springConfigs = new Class[]{scanModuleFunc.value()};
        return this;
    }

    public SpringInjectionRegistryBuilder springConfig(SpringModulesFunc scanModuleFunc) {
        ConfigResource configResource = new ConfigResource();
        springConfigs = scanModuleFunc.value(configResource);
        return this;
    }

    @Override
    public T getModule() {
        T module = super.getModule();
        module.setClasses(springConfigs);
        return module;
    }


}

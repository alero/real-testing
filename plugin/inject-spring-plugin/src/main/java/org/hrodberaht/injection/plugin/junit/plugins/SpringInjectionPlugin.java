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
import org.hrodberaht.injection.plugin.junit.spi.InjectionPlugin;
import org.hrodberaht.injection.plugin.junit.spi.Plugin;
import org.hrodberaht.injection.core.register.InjectionRegister;
import org.hrodberaht.injection.core.spi.ContainerConfigBuilder;

/**
 * The SpringInjectionPlugin is intended to not "start" a spring container but to
 * emulate the boot sequence of spring and move the beans found to the hrodberaht IoC container
 *
 * @see org.hrodberaht.injection.core.internal.InjectionContainer
 */
public class SpringInjectionPlugin implements InjectionPlugin, Plugin {

    public SpringInjectionPlugin() {
        throw new RuntimeException("Not yet supported");
    }

    @Override
    public void setInjectionRegister(InjectionRegister containerConfigBuilder) {

    }

    @Override
    public DefaultInjectionPointFinder getInjectionFinder(ContainerConfigBuilder containerConfigBuilder) {
        return null;
    }

    @Override
    public LifeCycle getLifeCycle() {
        return LifeCycle.TEST_SUITE;
    }
}

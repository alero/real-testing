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

package org.hrodberaht.injection.plugin.junit.plugins.megatest;

import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.plugins.JerseyPlugin;
import org.hrodberaht.injection.plugin.junit.plugins.test.service.JerseyApplication;
import org.hrodberaht.injection.plugin.junit.plugins.test.service.ObjectMapperResolver;

public class ContainerConfigMega extends ContainerContextConfigBase {

    @Override
    public void register(InjectionRegistryBuilder registryBuilder) {
        activatePlugin(new JerseyPlugin(Plugin.LifeCycle.TEST_CONFIG)).builder()
                .clientConfig(config -> config.register(ObjectMapperResolver.class))
                .resourceConfig(JerseyApplication::new);
    }
}

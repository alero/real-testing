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

package org.hrodberaht.injection.plugin.junit.plugins.test.service.config;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import org.hrodberaht.injection.plugin.junit.plugins.test.service.AService;
import org.hrodberaht.injection.plugin.junit.plugins.test.service.AnInterface;
import org.hrodberaht.injection.plugin.junit.plugins.test.service.InstanceInterface;
import org.hrodberaht.injection.plugin.junit.plugins.test.service.MoreServices;

public class GuiceModule extends AbstractModule {

    private InstanceInterface anInstance = () -> "something";

    @Override
    protected void configure() {
        bind(AnInterface.class).to(AService.class).asEagerSingleton();
        bind(MoreServices.class).asEagerSingleton();
        bind(InstanceInterface.class).toInstance(anInstance);

    }

    @Inject
    public void init(AService aService) {
        aService.init();
    }
}

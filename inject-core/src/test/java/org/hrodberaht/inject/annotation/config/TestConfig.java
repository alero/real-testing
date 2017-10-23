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

package test.org.hrodberaht.inject.annotation.config;

import org.hrodberaht.injection.core.InjectContainer;
import org.hrodberaht.injection.core.config.ContainerConfigBase;
import org.hrodberaht.injection.core.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.core.internal.ResourceInjection;
import org.hrodberaht.injection.core.register.InjectionRegister;
import org.hrodberaht.injection.core.spi.ResourceCreator;

/**
 * Created by alexbrob on 2016-03-29.
 */
public class TestConfig extends ContainerConfigBase {


    @Override
    protected ResourceCreator createResourceCreator() {
        return null;
    }

    @Override
    protected ResourceInjection createResourceInjector() {
        return null;
    }

    @Override
    public ResourceCreator getResourceCreator() {
        return null;
    }

    @Override
    public InjectContainer createContainer() {
        return null;
    }

    @Override
    protected void injectResources(Object serviceInstance) {

    }

    @Override
    protected InjectionRegisterScanBase getScanner(InjectionRegister registerModule) {
        return null;
    }

}

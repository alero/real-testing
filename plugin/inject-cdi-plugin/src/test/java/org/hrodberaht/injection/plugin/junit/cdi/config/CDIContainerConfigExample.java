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

package org.hrodberaht.injection.plugin.junit.cdi.config;

import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.CDIInjectionPlugin;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:37:42
 * @version 1.0
 * @since 1.0
 */
public class CDIContainerConfigExample extends ContainerContextConfigBase {

    public CDIContainerConfigExample() {
        activatePlugin(CDIInjectionPlugin.class);
    }

    @Override
    public void register(InjectionRegistryBuilder injectionRegistryBuilder) {
        injectionRegistryBuilder.scan(() -> "org.hrodberaht.injection.plugin.junit.cdi.service");
    }

    /*
    public CDIContainerConfigExample() {

        String dataSourceName = "MyDataSource";
        DataSource dataSource = createDataSource(dataSourceName);
        // Named resource
        addResourceCrator(dataSourceName, dataSource);

    }

    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("test.org.hrodberaht.inject.extension.cdi.service");
    }
*/

}

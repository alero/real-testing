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

import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.cdi.config.ioc.ExampleModuleExternal;
import org.hrodberaht.injection.plugin.junit.cdi.config.ioc.ExampleModuleInternal;
import org.hrodberaht.injection.plugin.junit.plugins.CDIInjectionPlugin;
import org.hrodberaht.injection.plugin.junit.plugins.JpaPlugin;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:37:42
 * @version 1.0
 * @since 1.0
 */
public class CDIContainerConfigExampleExternalResourcesAndConfig extends ContainerContextConfigBase {
    @Override
    public void register(InjectionRegistryBuilder injectionRegistryBuilder) {
        activatePlugin(CDIInjectionPlugin.class);
        JpaPlugin jpaPlugin = activatePlugin(new JpaPlugin.JpaPluginBuilder().usingJavaContext().build());
        new ExampleModuleInternal(injectionRegistryBuilder, jpaPlugin);
        new ExampleModuleExternal(injectionRegistryBuilder, jpaPlugin);

    }

    /*private CDIApplication cdiApplication = null;

    public CDIContainerConfigExampleExternalResourcesAndConfig() {
        cdiApplication = new CDIApplication(this);
        cdiApplication.add(new ExampleModuleExternal(this).getModule());
        cdiApplication.add(new ExampleModuleInternal(this).getModule());

        this.addSQLSchemas(ExampleModuleInternal.DATASOURCE, "test");
    }

    @Override
    public InjectContainer createContainer() {
        // This will actually register the container and run the CDI extensions
        return cdiApplication.createContainer();
    }
    */


}

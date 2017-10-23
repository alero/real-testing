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
import org.hrodberaht.injection.plugin.junit.plugins.JpaPlugin;
import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;

import javax.sql.DataSource;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:37:42
 * @version 1.0
 * @since 1.0
 */
public class CDIContainerConfigExampleExternalResources extends ContainerContextConfigBase {
    @Override
    public void register(InjectionRegistryBuilder injectionRegistryBuilder) {
        activatePlugin(CDIInjectionPlugin.class);
        JpaPlugin jpaPlugin = activatePlugin(JpaPlugin.class);
        DataSource dataSource = jpaPlugin.getCreator(DataSource.class).create("ExampleDataSource");
        jpaPlugin.createEntityManager("example-jpa");


        injectionRegistryBuilder
                .scan(() -> "org.hrodberaht.injection.extensions.cdi.example.service")
                .resource(builder -> builder.resource("ExampleDataSource", dataSource))
        ;
    }

    /*
    public CDIContainerConfigExampleExternalResources() {

        String dataSourceName = "ExampleDataSource";
        String jpaName = "example-jpa";
        DataSource dataSource = createDataSource(dataSourceName);
        // Named resource
        addResourceCrator(dataSourceName, dataSource);

        EntityManager entityManager = createEntityManager(jpaName, dataSourceName, dataSource);
        addPersistenceContext(jpaName, entityManager);

    }

    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("org.hrodberaht.injection.extensions.cdi.example.service");
    }
    */


}

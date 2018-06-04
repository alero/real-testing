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

package org.hrodberaht.injection.extensions.plugin.demo.test.config;

import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.JpaPlugin;

import javax.sql.DataSource;

public class CourseContainerConfigExample extends ContainerContextConfigBase {

    public static final String DATASOURCE_NAME = "MyDataSource";


    @Override
    public void register(InjectionRegistryBuilder registryBuilder) {
        JpaPlugin jpaPlugin = activatePlugin(new JpaPlugin.JpaPluginBuilder().usingJavaContext().build());
        DataSource dataSource = jpaPlugin.createDataSource(DATASOURCE_NAME);

        jpaPlugin.createEntityManager(dataSource, "example-jpa");

        // Load schema is a custom method located in the plugin code, this creates clean separation
        jpaPlugin
                .loadSchema(dataSource, "org.hrodberaht.injection.extensions.plugin.course")
        ;


        registryBuilder
                .scan(() -> "org.hrodberaht.injection.extensions.plugin.demo.service")
                .resource(builder ->
                        builder
                                .resource("MyDataSource", DataSource.class, dataSource)

                )
        ;
    }
}

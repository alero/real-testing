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

package org.hrodberaht.injection.extensions.plugin.junit.datasource.test.config;

import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.DataSourcePlugin;

import javax.sql.DataSource;

public class SQLLoadingContainerConfig extends ContainerContextConfigBase {


    @Override
    public void register(InjectionRegistryBuilder registryBuilder) {

        DataSourcePlugin dataSourcePlugin = activatePlugin(DataSourcePlugin.class);
        DataSource dataSource = dataSourcePlugin.createDataSource("MyDataSource");

        // Load schema is a custom method located in the plugin code, this creates clean separation
        dataSourcePlugin
                .loadSchema(dataSource, "org.hrodberaht.injection.extensions.plugin.course2")
        // .loadSchema(dataSource, "org.hrodberaht.injection.extensions.plugin.course2")
        ;


        registryBuilder
                .scan(() -> "org.hrodberaht.injection.extensions.plugin.junit.datasource.service")
                .resource(builder ->
                        builder
                                .resource("MyDataSource", DataSource.class, dataSource)
                                .resource(DataSource.class, dataSource)
                )
        ;
    }


}

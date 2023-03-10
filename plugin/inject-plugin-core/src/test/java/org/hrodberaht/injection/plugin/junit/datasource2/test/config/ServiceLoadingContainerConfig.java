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

package org.hrodberaht.injection.plugin.junit.datasource2.test.config;

import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.datasource2.test.LoadingTheTestWithData;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.DataSourcePlugin;

import javax.sql.DataSource;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 * 2011-05-03 20:31
 * @created 1.0
 * @since 1.0
 */
public class ServiceLoadingContainerConfig extends ContainerContextConfigBase {


    @Override
    public void register(InjectionRegistryBuilder registryBuilder) {

        DataSourcePlugin dataSourcePlugin = activatePlugin(DataSourcePlugin.class);

        DataSource dataSource = dataSourcePlugin.createDataSource("MyDataSource2");

        // Load schema is a custom method located in the plugin code, this creates clean separation
        dataSourcePlugin
                .loadSchema(dataSource, "org.hrodberaht.injection.plugin.course")
                .addBeforeTestSuite(LoadingTheTestWithData.class)
        ;


        registryBuilder
                .scan(() -> "org.hrodberaht.injection.plugin.junit.datasource2.service")
                .resource(builder ->
                        builder
                                // .bindPluginResources()
                                .resource("MyDataSource2", DataSource.class, dataSource)
                                .resource(DataSource.class, dataSource)

                )
        ;
    }


}

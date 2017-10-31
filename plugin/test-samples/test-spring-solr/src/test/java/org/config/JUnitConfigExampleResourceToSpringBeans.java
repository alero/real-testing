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

package org.config;


import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.plugins.DataSourcePlugin;
import org.hrodberaht.injection.plugin.junit.plugins.SolrJPlugin;
import org.hrodberaht.injection.plugin.junit.plugins.SpringExtensionPlugin;

import javax.sql.DataSource;

public class JUnitConfigExampleResourceToSpringBeans extends ContainerContextConfigBase {


    @Override
    public void register(InjectionRegistryBuilder registryBuilder) {
        // TODO: change to use "no help" resourceNaming convention as default.
        String dataSourceName = "MyDataSource";
        DataSourcePlugin dataSourcePlugin = activatePlugin(DataSourcePlugin.class);
        DataSource dataSource = dataSourcePlugin.createDataSource(dataSourceName);

        dataSourcePlugin.loadSchema(dataSource, "sql");
        dataSourcePlugin.addBeforeTestSuite((loader) -> loader.get(LoadingTheTestWithData.class).run());

        SolrJPlugin solrJPlugin = activatePlugin(SolrJPlugin.class)
                .solrHome(SolrJPlugin.DEFAULT_HOME)
                .coreName("collection1");

        activatePlugin(SpringExtensionPlugin.class)
                // .lifeCycle(Plugin.ResourceLifeCycle.TEST_SUITE)
                .withDataSource(dataSourcePlugin)
                    .datasource().commitAfterContainerCreation()
                    .datasource().resourceAsSpringBeans()
                .withSolr(solrJPlugin)
                    .solr().resourceAsSpringBeans()
                .springConfig(SpringConfigJavaSample2.class);

    }
}

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

package org.hrodberaht.injection.plugin.junit.config;

import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.LiquibasePlugin;

import javax.sql.DataSource;

public class ContainerConfigExample extends ContainerContextConfigBase {

    @Override
    public void register(InjectionRegistryBuilder registryBuilder) {
        LiquibasePlugin liquibasePlugin = activatePlugin(LiquibasePlugin.class);
        DataSource dataSource = liquibasePlugin.createDataSource();

        liquibasePlugin.load(dataSource, "db.changelog-test.xml");

        liquibasePlugin
                .loadSchema(dataSource, "sample");

        registryBuilder.resource(builder ->
                builder.resource(DataSource.class, dataSource)
        );
    }
}

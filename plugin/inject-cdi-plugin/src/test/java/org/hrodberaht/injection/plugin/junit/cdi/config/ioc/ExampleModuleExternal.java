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

package org.hrodberaht.injection.plugin.junit.cdi.config.ioc;

import org.hrodberaht.injection.Module;
import org.hrodberaht.injection.plugin.junit.plugins.JpaPlugin;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

import javax.sql.DataSource;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class ExampleModuleExternal {

    public static final String DATASOURCE = "ExampleDataSource";

    private Module module;

    public ExampleModuleExternal(InjectionRegistryBuilder configBase, JpaPlugin jpaPlugin) {
        DataSource dataSource = jpaPlugin.getCreator(DataSource.class).create(DATASOURCE);
        jpaPlugin.createEntityManager("example-jpa");
        module = configBase
                .scan(() -> "org.hrodberaht.injection.extensions.cdi.example.service")
                .resource(builder -> builder.resource(DATASOURCE, dataSource))
                .getModule();
    }

    public Module getModule() {
        return module;
    }
}

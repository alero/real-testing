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

package org.hrodberaht.injection.extensions.plugin.jpa;


import org.hrodberaht.injection.extensions.plugin.jpa.spring.PersistenceJPAConfig;
import org.hrodberaht.injection.extensions.plugin.jpa.spring.SpringBean;
import org.hrodberaht.injection.extensions.plugin.jpa.spring.SpringConfig;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.JUnit4Runner;
import org.hrodberaht.injection.plugin.junit.plugins.JpaPlugin;
import org.hrodberaht.injection.plugin.junit.plugins.SpringExtensionPlugin;
import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;

@ContainerContext(TestSpringJPAPlugin.Config.class)
@RunWith(JUnit4Runner.class)
public class TestSpringJPAPlugin {

    public static class Config extends ContainerContextConfigBase {

        @Override
        public void register(InjectionRegistryBuilder registryBuilder) {

            SpringExtensionPlugin springExtensionPlugin = activatePlugin(SpringExtensionPlugin.class);

            JpaPlugin jpaPlugin = activatePlugin(JpaPlugin.class);
            jpaPlugin.getCreator(DataSource.class).create("MyDataSource");
            // jpaPlugin.createEntityManager("example-jpa");

            springExtensionPlugin
                    .enableJPA()
                    // .withDataSource("MyDataSource")
                    .loadConfig(SpringConfig.class, PersistenceJPAConfig.class);

        }
    }

    @Autowired
    private SpringBean springBean;

    @Test
    public void testForUserCreation() throws Exception {
        springBean.createUser("dude", "hi");

        assertEquals("hi", springBean.getUser("dude").getPassword());

    }
}

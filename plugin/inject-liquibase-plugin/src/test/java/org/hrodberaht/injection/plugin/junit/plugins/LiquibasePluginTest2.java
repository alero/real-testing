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

package org.hrodberaht.injection.plugin.junit.plugins;

import org.hrodberaht.injection.plugin.datasource.jdbc.JDBCService;
import org.hrodberaht.injection.plugin.datasource.jdbc.JDBCServiceFactory;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnit4Runner;
import org.hrodberaht.injection.plugin.junit.config.ContainerConfigExample;
import org.hrodberaht.injection.plugin.junit.config.ContainerConfigExample2;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContainerContext(ContainerConfigExample2.class)
@RunWith(JUnit4Runner.class)
public class LiquibasePluginTest2 {
    @Resource
    private DataSource dataSource;

    private JDBCService jdbcService;

    @PostConstruct
    public void init() {
        jdbcService = JDBCServiceFactory.of(dataSource);
    }

    @Test
    public void loadFromSampleFile() throws Exception {
        List<String> stringList =
                jdbcService.query("select * from simple", (rs, iteration) -> rs.getString("name"));

        assertEquals(1, stringList.size());

        assertTrue(Paths.get("target/liquibase/main/backup.script").toFile().exists());

        assertTrue(Paths.get("target/liquibase/main/filewatcher.log").toFile().exists());

    }
}
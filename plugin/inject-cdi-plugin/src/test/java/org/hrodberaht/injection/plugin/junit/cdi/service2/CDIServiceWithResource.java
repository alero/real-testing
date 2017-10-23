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

package org.hrodberaht.injection.plugin.junit.cdi.service2;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Singleton;
import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-12-04
 * Time: 08:36
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class CDIServiceWithResource {

    @Resource(name = "MyDataSource")
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate = null;

    @PostConstruct
    public void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void createStuff(long id, String value) {
        jdbcTemplate.update("insert into theTable values (?, ?)", id, value);
    }

    public String findStuff(long id) {
        return jdbcTemplate.queryForObject("select name from theTable where id = ?", String.class, id);
    }
}

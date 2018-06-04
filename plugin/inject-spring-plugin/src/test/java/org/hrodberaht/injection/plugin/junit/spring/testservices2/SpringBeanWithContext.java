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

package org.hrodberaht.injection.plugin.junit.spring.testservices2;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

@Repository
public class SpringBeanWithContext {


    @Resource(lookup = "MyDataSource2")
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        synchronized (SpringBeanWithSpringBean.class) {
            if (getName("init") == null) {
                createUser("init", "user");
            }
        }
    }

    public String getName() {
        return "SpringBeanName";
    }

    public String getName(String name) {
        try {
            return jdbcTemplate.queryForObject("select username from theUser where username=?", String.class, name);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void createUser(String username, String password) {
        jdbcTemplate.update("insert into theUser (username, password) values (?, ?)", username, password);
    }

}

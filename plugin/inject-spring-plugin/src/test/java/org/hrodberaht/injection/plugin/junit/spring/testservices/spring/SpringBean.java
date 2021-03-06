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

package org.hrodberaht.injection.plugin.junit.spring.testservices.spring;

import org.hrodberaht.injection.plugin.junit.spring.injector.SpringInject;
import org.hrodberaht.injection.plugin.junit.spring.testservices.simple.AnyServiceInner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

@Repository
public class SpringBean {

    @SpringInject
    private AnyServiceInner anyServiceInner;

    @Resource(lookup = "MyDataSource")
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String getName() {
        return "SpringBeanName";
    }

    public String getNameFromDB() {
        return jdbcTemplate.queryForObject("select username from theUser where username=?", String.class, "dude");
    }

    public AnyServiceInner getAnyServiceInner() {
        return anyServiceInner;
    }
}

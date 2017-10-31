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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

@Repository
public class SpringBeanWithSpringBean {


    @Autowired()
    @Qualifier("DataSource/MyDataSource2")
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        synchronized (SpringBeanWithSpringBean.class){
            if( getName("init") == null){
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
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public Integer getLoginCount(String name) {
        try {
            return jdbcTemplate.queryForObject("select loginTries from theUser where username=?", Integer.class, name);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public boolean checkPassword(String username, String password) {
        try {
            jdbcTemplate.queryForObject("select username from theUser where username=? and password=?", String.class, username, password);
            return true;
        }catch (EmptyResultDataAccessException e){
            return false;
        }
    }

    public void createUser(String username, String password) {
        jdbcTemplate.update("insert into theUser (username, password, loginTries) values (?, ?, ?)", username, password, 0);
    }

    public boolean login(String username, String password) {
        jdbcTemplate.update("update theUser set loginTries = loginTries + 1");
        if(checkPassword(username, password)){
            jdbcTemplate.update("update theUser set loginTries = 0");
            return true;
        }
        return false;
    }

}

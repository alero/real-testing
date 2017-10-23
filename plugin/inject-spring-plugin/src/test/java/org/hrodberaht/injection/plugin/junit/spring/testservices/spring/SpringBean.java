package org.hrodberaht.injection.plugin.junit.spring.testservices.spring;

import org.hrodberaht.injection.plugin.junit.spring.injector.SpringInject;
import org.hrodberaht.injection.plugin.junit.spring.testservices.simple.AnyServiceInner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by alexbrob on 2016-03-29.
 */
@Repository
public class SpringBean {

    @SpringInject
    private AnyServiceInner anyServiceInner;

    @Resource(lookup = "MyDataSource2")
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

    public void createUser(String username, String password) {
        jdbcTemplate.update("insert into theUser (username, password) values (?, ?)", username, password);
    }

    public AnyServiceInner getAnyServiceInner() {
        return anyServiceInner;
    }
}

package org.hrodberaht.injection.extensions.spring.testservices.spring;

import org.hrodberaht.injection.extensions.spring.instance.SpringInject;
import org.hrodberaht.injection.extensions.spring.testservices.simple.AnyServiceInner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Created by alexbrob on 2016-03-29.
 */
@Component
public class SpringBean {

    @SpringInject
    private AnyServiceInner anyServiceInner;

    @Autowired
    @Qualifier("MyDataSource")
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String getName(){
        return "SpringBeanName";
    }

    public String getNameFromDB(){
        return jdbcTemplate.queryForObject("select username from user where username=?", String.class, "dude");
    }

    public AnyServiceInner getAnyServiceInner() {
        return anyServiceInner;
    }
}

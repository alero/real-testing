package org.hrodberaht.injection.extensions.spring.testservices2;

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
        return jdbcTemplate.queryForObject("select username from user where username=?", String.class, "dude");
    }

}

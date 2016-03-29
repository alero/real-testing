package org.hrodberaht.inject.extensions.junit.demo2.service;

import org.hrodberaht.inject.extensions.junit.demo2.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Qmatic Booking Module
 *
 * @author Robert Alexandersson
 *         2011-05-08 01:53
 * @created 1.0
 * @since 1.0
 */
@Stateless
public class UserServiceBean implements UserService {

    @Resource(mappedName = "MyDataSource")
    DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User find(String userName) {
        try {
            return jdbcTemplate.queryForObject(
                    "select * from user where username = ?", new UserRowMapper(), userName);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean loginCompare(String userName, String password) {
        User user = find(userName);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }


    private class UserRowMapper implements RowMapper<User> {

        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            return new User(resultSet.getString("userName"), resultSet.getString("password"));
        }
    }
}

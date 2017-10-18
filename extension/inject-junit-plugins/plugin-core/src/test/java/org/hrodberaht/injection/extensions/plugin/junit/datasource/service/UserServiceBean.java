package org.hrodberaht.injection.extensions.plugin.junit.datasource.service;

import org.hrodberaht.injection.extensions.plugin.junit.model.User;
import org.hrodberaht.injection.plugin.datasource.jdbc.InsertOrUpdater;
import org.hrodberaht.injection.plugin.datasource.jdbc.JDBCService;
import org.hrodberaht.injection.plugin.datasource.jdbc.JDBCServiceFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Singleton;
import javax.sql.DataSource;

/**
 * Qmatic Booking Module
 *
 * @author Robert Alexandersson
 * 2011-05-08 01:53
 * @created 1.0
 * @since 1.0
 */
@Singleton
public class UserServiceBean implements UserService {

    @Resource
    private DataSource dataSource;

    @Resource(name = "MyDataSource")
    private DataSource namedDataSource;

    private JDBCService jdbcService;


    @PostConstruct
    public void init() {
        jdbcService = JDBCServiceFactory.of(dataSource);
    }

    public User find(String userName) {

            return jdbcService.querySingle(
                    "select * from user where username = ?",
                    (rs, iteration) -> new User(rs.getString("username"), rs.getString("password")),
                    userName);

    }

    public void changePassword(String userName, String newPassword) {
        InsertOrUpdater insertOrUpdater = jdbcService.createInsertOrUpdate("user");
        insertOrUpdater.where("username", userName);
        insertOrUpdater.field("password", newPassword);
        jdbcService.insertOrUpdate(insertOrUpdater);
    }

    public boolean loginCompare(String userName, String password) {
        User user = find(userName);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }

    @Override
    public DataSource getTypedDataSource() {
        return dataSource;
    }

    @Override
    public DataSource getNamedDataSource() {
        return namedDataSource;
    }

}

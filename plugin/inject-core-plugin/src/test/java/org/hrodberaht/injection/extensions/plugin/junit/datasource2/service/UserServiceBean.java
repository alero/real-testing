package org.hrodberaht.injection.extensions.plugin.junit.datasource2.service;

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

    @Resource(name = "MyDataSource2")
    private DataSource namedDataSource;

    private JDBCService jdbcService;


    @PostConstruct
    public void init() {
        jdbcService = JDBCServiceFactory.of(namedDataSource);
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
    public DataSource getNamedDataSource() {
        return namedDataSource;
    }

    @Override
    public void createUser(String userName, String password) {
        InsertOrUpdater insertOrUpdater = jdbcService.createInsertOrUpdate("user");
        insertOrUpdater.field("username", userName);
        insertOrUpdater.field("password", password);
        jdbcService.insertOrUpdate(insertOrUpdater);
    }

}

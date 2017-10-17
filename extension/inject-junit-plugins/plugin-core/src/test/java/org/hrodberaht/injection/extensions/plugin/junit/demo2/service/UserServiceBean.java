package org.hrodberaht.injection.extensions.plugin.junit.demo2.service;

import org.hrodberaht.injection.extensions.plugin.junit.demo2.model.User;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Qmatic Booking Module
 *
 * @author Robert Alexandersson
 * 2011-05-08 01:53
 * @created 1.0
 * @since 1.0
 */
public class UserServiceBean implements UserService {

    Map<String, User> userMap = new HashMap<>();

    @Resource
    private MyResource myTypedResource;

    @Resource(name = "myResource")
    private MyResource myNamedResource;

    @PostConstruct
    public void init() {
        User user = new User("dude", "wheremycar");
        userMap.put(user.getUserName(), user);
    }

    public User find(String userName) {
        return userMap.get(userName);
    }

    public boolean loginCompare(String userName, String password) {
        User user = find(userName);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }


    public MyResource getMyTypedResource() {
        return myTypedResource;
    }

    public MyResource getMyNamedResource() {
        return myNamedResource;
    }
}

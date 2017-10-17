package org.hrodberaht.injection.extensions.plugin.junit.demo2.service;

import org.hrodberaht.injection.extensions.plugin.junit.demo2.model.User;

/**
 * Qmatic Booking Module
 *
 * @author Robert Alexandersson
 * 2011-05-08 01:53
 * @created 1.0
 * @since 1.0
 */


public interface UserService {

    User find(String userName);

    boolean loginCompare(String userName, String password);

    MyResource getMyTypedResource();

    MyResource getMyNamedResource();

}

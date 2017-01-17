package org.hrodberaht.injection.extensions.junit.demo2.service;

/**
 * Qmatic Booking Module
 *
 * @author Robert Alexandersson
 * 2011-05-08 01:53
 * @created 1.0
 * @since 1.0
 */

import org.hrodberaht.injection.extensions.junit.demo2.model.User;

import javax.ejb.Local;

@Local
public interface UserService {

    User find(String userName);

    boolean loginCompare(String userName, String password);

}

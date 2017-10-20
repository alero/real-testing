package org.hrodberaht.injection.extensions.plugin.junit.datasource2.test;

import org.hrodberaht.injection.extensions.plugin.junit.datasource2.service.UserService;

import javax.inject.Inject;

public class LoadingTheTestWithData {

    @Inject
    private UserService userService;

    public void load() {
        userService.createUser("dude", "wheremycar");
    }


}

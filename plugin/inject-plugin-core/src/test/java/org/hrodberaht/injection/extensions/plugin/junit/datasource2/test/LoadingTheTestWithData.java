package org.hrodberaht.injection.extensions.plugin.junit.datasource2.test;

import org.hrodberaht.injection.extensions.plugin.junit.datasource2.service.UserService;
import org.hrodberaht.injection.plugin.junit.plugins.DataSourcePlugin;

import javax.inject.Inject;

public class LoadingTheTestWithData implements DataSourcePlugin.ResourceLoaderRunner {

    @Inject
    private UserService userService;

    @Override
    public void run() {
        userService.createUser("dude", "wheremycar");
    }


}

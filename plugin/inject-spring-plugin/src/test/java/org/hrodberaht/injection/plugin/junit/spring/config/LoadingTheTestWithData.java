package org.hrodberaht.injection.plugin.junit.spring.config;

import org.hrodberaht.injection.plugin.junit.plugins.DataSourcePlugin;
import org.hrodberaht.injection.plugin.junit.spring.testservices.spring.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;

public class LoadingTheTestWithData implements DataSourcePlugin.ResourceLoaderRunner {

    @Autowired
    private SpringBean springBean;

    public void run() {
        springBean.createUser("dude", "wheremycar");
    }
}

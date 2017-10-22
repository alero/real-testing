package org.hrodberaht.injection.extensions.plugin.demo.test.util;

import org.hrodberaht.injection.extensions.plugin.demo.test.config.CourseContainerConfigExample;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnitRunner;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import static org.hrodberaht.injection.extensions.plugin.demo.test.config.CourseContainerConfigExample.DATASOURCE_NAME;

@ContainerContext(CourseContainerConfigExample.class)
@RunWith(JUnitRunner.class)
public abstract class AbstractBaseClass {

    @Resource(name = DATASOURCE_NAME)
    private DataSource dataSource;

    protected String init = null;

    @PostConstruct
    protected void init() {
        init = "initiated";
    }

}

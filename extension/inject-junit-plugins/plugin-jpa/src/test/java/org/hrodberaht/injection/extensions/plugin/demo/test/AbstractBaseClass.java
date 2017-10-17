package org.hrodberaht.injection.extensions.plugin.demo.test;

import org.hrodberaht.injection.extensions.plugin.demo.test.config.CourseContainerConfigExample;
import org.hrodberaht.injection.extensions.plugin.test.PluggableJUnitRunner;
import org.hrodberaht.injection.extensions.plugin.test.ContainerContext;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;

@ContainerContext(CourseContainerConfigExample.class)
@RunWith(PluggableJUnitRunner.class)
public abstract class AbstractBaseClass {

    protected String init = null;

    @PostConstruct
    protected void init() {
        init = "initiated";
    }

}

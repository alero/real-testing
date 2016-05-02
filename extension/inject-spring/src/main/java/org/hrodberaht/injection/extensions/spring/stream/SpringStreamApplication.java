package org.hrodberaht.injection.extensions.spring.stream;

import org.hrodberaht.injection.Module;
import org.hrodberaht.injection.extensions.spring.SpringApplication;
import org.hrodberaht.injection.extensions.spring.SpringContainerConfigBase;

public abstract class SpringStreamApplication {

    protected SpringApplication springApplication;

    private SpringContainerConfigBase springContainerConfigBase = createSpringContainerConfigBase();

    protected abstract SpringContainerConfigBase createSpringContainerConfigBase();

    protected SpringApplication createSpringApplication() {
        return new SpringApplication(springContainerConfigBase);
    }

    protected void add(Module module) {
        springApplication.add(module);
    }

    public SpringApplication getSpringApplication() {
        return springApplication;
    }
}

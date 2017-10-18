package org.hrodberaht.injection.plugin.junit.spring.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.Module;

public class SpringModule extends Module {

    private Class[] classes;

    public SpringModule(InjectContainer container) {
        super(container);
    }

    public Class[] getClasses() {
        return classes;
    }

    public void setClasses(Class[] classes) {
        this.classes = classes;
    }
}

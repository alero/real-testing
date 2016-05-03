package org.hrodberaht.injection.extensions.spring;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.Module;

import java.util.ArrayList;
import java.util.List;

public class SpringApplication {

    private List<Module> moduleList = new ArrayList<>();
    private SpringContainerConfigBase containerConfigBase;

    public SpringApplication(SpringContainerConfigBase containerConfigBase) {
        this.containerConfigBase = containerConfigBase;
    }

    public void add(Module module) {
        moduleList.add(module);
    }

    public InjectContainer createContainer() {

        InjectContainer injectContainer = containerConfigBase.loadModule(moduleList);
        for (Module module : moduleList) {
            if (module instanceof SpringModule) {
                containerConfigBase.loadJavaSpringConfig(((SpringModule) module).getClasses());
            }
        }


        return containerConfigBase.createContainer();
    }

}

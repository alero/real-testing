package org.hrodberaht.injection.extensions.cdi;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexbrob on 2016-03-31.
 */
public class CDIApplication  {

    private List<Module> moduleList = new ArrayList<>();
    private CDIContainerConfigBase containerConfigBase;

    public CDIApplication(CDIContainerConfigBase containerConfigBase) {
        this.containerConfigBase = containerConfigBase;
    }

    public void add(Module module) {
        moduleList.add(module);
    }

    public InjectContainer createContainer() {
        containerConfigBase.runBeforeBeanDiscovery();
        InjectContainer injectContainer = containerConfigBase.loadModule(moduleList);
        containerConfigBase.runAfterBeanDiscovery();
        return injectContainer;
    }
}

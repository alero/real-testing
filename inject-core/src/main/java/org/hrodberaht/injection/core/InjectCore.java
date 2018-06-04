package org.hrodberaht.injection.core;

import org.hrodberaht.injection.core.internal.InjectionRegisterModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InjectCore {
    public static InjectContainer asInjector(AbstractModule... abstractModules) {
        return asInjectionRegisterModule(Arrays.asList(abstractModules)).getContainer();
    }

    public static InjectContainer asInjector(List<AbstractModule> abstractModules) {
        return asInjectionRegisterModule(abstractModules).getContainer();
    }

    public static InjectionRegisterModule asInjectionRegisterModule(List<AbstractModule> abstractModules) {
        List<Module> moduleList = getModules(abstractModules);
        return new InjectionRegisterModule().register(moduleList);
    }

    private static List<Module> getModules(List<AbstractModule> abstractModules) {
        List<Module> moduleList = new ArrayList<>();
        for (AbstractModule abstractModule : abstractModules) {
            moduleList.add(Module.toModule(abstractModule));
        }
        return moduleList;
    }
}

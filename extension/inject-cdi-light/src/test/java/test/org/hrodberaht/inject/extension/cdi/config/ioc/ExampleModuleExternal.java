package test.org.hrodberaht.inject.extension.cdi.config.ioc;

import org.hrodberaht.injection.extensions.cdi.CDIContainerConfigBase;
import org.hrodberaht.injection.extensions.cdi.CDIModule;
import org.hrodberaht.injection.extensions.cdi.example.service.DataSourceNames;
import org.hrodberaht.injection.extensions.cdi.stream.CDIInjectionRegistryStream;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class ExampleModuleExternal {

    private CDIModule module;

    public ExampleModuleExternal(CDIContainerConfigBase configBase) {
        module =  new CDIInjectionRegistryStream(configBase)
                .scan(() -> "org.hrodberaht.injection.extensions.cdi.example.service")
                .resource(e -> {
                        e.createDataSource(DataSourceNames.SAMPLE);
                        e.createEntityManager("example-jpa", DataSourceNames.SAMPLE);
                    }
                )
                .getModule();
    }

    public CDIModule getModule() {
        return module;
    }
}

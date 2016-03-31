package test.org.hrodberaht.inject.extension.cdi.config.ioc;

import org.hrodberaht.injection.extensions.cdi.CDIContainerConfigBase;
import org.hrodberaht.injection.extensions.cdi.CDIModule;
import org.hrodberaht.injection.extensions.cdi.stream.CDIInjectionRegistryStream;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class ExampleModuleInternal {

    public static final String DATASOURCE = "MyDataSource";

    private CDIModule module;

    public ExampleModuleInternal(CDIContainerConfigBase configBase) {
        module =  new CDIInjectionRegistryStream(configBase)
                .scan(() -> "test.org.hrodberaht.inject.extension.cdi.service2")
                .resource(e -> {
                        e.createDataSource(DATASOURCE);
                    }
                )
                .getModule();
    }

    public CDIModule getModule() {
        return module;
    }
}

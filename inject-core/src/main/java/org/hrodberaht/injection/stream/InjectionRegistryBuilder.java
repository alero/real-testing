package org.hrodberaht.injection.stream;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.InjectionRegistry;
import org.hrodberaht.injection.Module;
import org.hrodberaht.injection.config.ContainerConfig;
import org.hrodberaht.injection.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.config.InjectionStoreFactory;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.internal.RegistrationInstanceSimple;
import org.hrodberaht.injection.spi.ResourceKey;
import sun.misc.Resource;

import java.util.List;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class InjectionRegistryBuilder<T extends Module> implements InjectionRegistry<T> {

    private InjectContainer injectionContainer;
    private InjectionRegister injectionRegisterModule = InjectionStoreFactory.getInjectionRegister();
    private ContainerConfig configBase;

    public InjectionRegistryBuilder(ContainerConfig configBase) {
        this.configBase = configBase;
    }

    public InjectionRegistryBuilder() {
    }

    public InjectContainer getContainer() {
        return injectionContainer;
    }

    public InjectionRegisterScanBase getCustomScanner(){
        return null;
    }

    public InjectionRegistryBuilder module(AppendModuleFunc scanModuleFunc){
        injectionRegisterModule.register(scanModuleFunc.module());
        injectionContainer = injectionRegisterModule.getContainer();
        return this;
    }

    public InjectionRegistryBuilder scan(ScanModulesFunc scanModuleFunc) {
        Packaging packaging = new Packaging();
        String[] _packages = scanModuleFunc.scan(packaging);
        Module module = new Module() {
            @Override
            public void scan() {
                this.scanAndRegister(_packages);
            }

            @Override
            public InjectionRegisterScanBase getScanner() {
                InjectionRegisterScanBase registerScan = getCustomScanner();
                if (registerScan != null) {
                    return registerScan;
                }
                return super.getScanner();
            }
        };
        injectionRegisterModule.register(module);
        injectionContainer = injectionRegisterModule.getContainer();
        return this;
    }

    public InjectionRegistryBuilder scan(ScanModuleFunc scanModuleFunc){
        String _packages = scanModuleFunc.scan();
        Module module = new Module() {
            @Override
            public void scan() {
                this.scanAndRegister(_packages);
            }

            @Override
            public InjectionRegisterScanBase getScanner() {
                InjectionRegisterScanBase registerScan = getCustomScanner();
                if(registerScan != null){
                    return registerScan;
                }
                return super.getScanner();
            }
        };
        injectionRegisterModule.register(module);
        injectionContainer = injectionRegisterModule.getContainer();
        return this;
    }

    public InjectionRegistryBuilder register(RegisterModuleFunc scanModuleFunc){
        Registrations registrations = new Registrations();
        scanModuleFunc.register(registrations);
        List<RegistrationInstanceSimple> register = registrations.registry();
        Module module = new Module() {
            @Override
            public void registrations() {
                this.putRegistrations(register);
            }
        };
        injectionRegisterModule.register(module);
        registrations.modules().forEach(registrationModuleAnnotation -> {
            injectionRegisterModule.register(registrationModuleAnnotation);
        });

        injectionContainer = injectionRegisterModule.getContainer();
        return this;
    }

    public InjectionRegistryBuilder resource(ResourceFunc registerResourceFunc) {
        if(configBase == null){
            throw new IllegalAccessError("ContainerConfigBase needed for resources");
        }
        ResourcesBuilder registrations = new ResourcesBuilder();
        registerResourceFunc.createResource(registrations);

        /*for(ResourceEntityManager resourceEntityManager:registrations.getEntityManagers()) {
            String dataSourceName = resourceEntityManager.getResourceDataSource().getName();
            DataSource dataSource = createRegisterDataSource(resourceEntityManager.getResourceDataSource());

            String entityManagerName = resourceEntityManager.getName();
            EntityManager entityManager = configBase.
                    createEntityManager(entityManagerName, dataSourceName, dataSource);
            configBase.addPersistenceContext(entityManagerName, entityManager);

        }*/

        /*for(ResourceDataSource resourceDataSource:registrations.getDataSources()) {
            createRegisterDataSource(resourceDataSource);
        }*/
        registrations.getNamedInstances().entrySet().forEach(
                entry -> configBase.getResourceFactory()
                        .getCreator(entry.getKey().getType())
                            .create(entry.getKey().getName(), entry.getValue())
        );

        registrations.getTypedInstances().entrySet().forEach(
                entry -> configBase.getResourceFactory()
                        .getCreator(entry.getKey())
                        .create(entry.getValue())
        );

        return this;
    }
/*
    private DataSource createRegisterDataSource(ResourceDataSource resourceDataSource) {
        String dataSourceName = resourceDataSource.getName();
        // Makes it possible to define the same datasource in many multiple junit runners
        if (!configBase.hasDataSource(dataSourceName)) {
            DataSource dataSource = configBase.createDataSource(dataSourceName);
            configBase.addResource(dataSourceName, dataSource);
            return dataSource;
        }
        return configBase.getDataSource(dataSourceName);
    }
*/
    public T getModule() {
        Module module = createModuleContainer();
        injectionRegisterModule.fillModule(module);
        return (T) module;
    }

    protected T createModuleContainer(){
        return (T) new Module(injectionContainer);
    }

    public InjectionRegister build() {
        return InjectionStoreFactory.getInjectionRegister(injectionRegisterModule).register(getModule());
    }
}

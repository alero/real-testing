package org.hrodberaht.injection.stream;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.InjectionRegistry;
import org.hrodberaht.injection.Module;
import org.hrodberaht.injection.config.InjectionRegisterScanBase;
import org.hrodberaht.injection.config.jpa.JPAContainerConfigBase;
import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.register.internal.RegistrationInstanceSimple;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class InjectionRegistryStream<T extends Module> implements InjectionRegistry<T> {

    private InjectContainer injectionContainer;
    private InjectionRegisterModule injectionRegisterModule = new InjectionRegisterModule();
    private JPAContainerConfigBase configBase;

    public InjectionRegistryStream(JPAContainerConfigBase configBase) {
        this.configBase = configBase;
    }

    public InjectionRegistryStream() {
    }

    public InjectContainer getContainer() {
        return injectionContainer;
    }

    public InjectionRegisterScanBase getCustomScanner(){
        return null;
    }

    public InjectionRegistryStream scan(ScanModuleFunc scanModuleFunc){
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

    public InjectionRegistryStream register(RegisterModuleFunc scanModuleFunc){
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
        injectionContainer = injectionRegisterModule.getContainer();
        return this;
    }

    public InjectionRegistryStream resource(RegisterResourceFunc registerResourceFunc) {
        if(configBase == null){
            throw new IllegalAccessError("ContainerConfigBase needed for resources");
        }
        InjectionResources registrations = new InjectionResources();
        registerResourceFunc.createResource(registrations);

        for(ResourceEntityManager resourceEntityManager:registrations.getEntityManagers()) {
            String dataSourceName = resourceEntityManager.getResourceDataSource().getName();
            DataSource dataSource = createRegisterDataSource(resourceEntityManager.getResourceDataSource());

            String entityManagerName = resourceEntityManager.getName();
            EntityManager entityManager = configBase.
                    createEntityManager(entityManagerName, dataSourceName, dataSource);
            configBase.addPersistenceContext(entityManagerName, entityManager);

        }

        for(ResourceDataSource resourceDataSource:registrations.getDataSources()) {
            createRegisterDataSource(resourceDataSource);
        }


        return this;
    }

    private DataSource createRegisterDataSource(ResourceDataSource resourceDataSource) {
        String dataSourceName = resourceDataSource.getName();
        // Makes it possible to define the same datasource in many multiple junit runners
        if (!configBase.hasDataSource(dataSourceName)) {
            DataSource dataSource = configBase.createDataSource(dataSourceName);
            configBase.addResource(dataSourceName, dataSource);
            /* This must be part of the testing "stream" somehow
            configBase.addSQLSchemas(
                    resourceDataSource.getPackageName(),
                    dataSourceName,
                    resourceDataSource.getPath()
            );
            */
            return dataSource;
        }
        return configBase.getDataSource(dataSourceName);
    }

    public T getModule() {
        Module module = createModuleContainer();
        injectionRegisterModule.fillModule(module);
        return (T) module;
    }

    protected T createModuleContainer(){
        return (T) new Module(injectionContainer);
    }
}

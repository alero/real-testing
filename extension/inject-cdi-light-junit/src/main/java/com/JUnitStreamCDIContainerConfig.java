package com;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.cdi.CDIContainerConfigBase;
import org.hrodberaht.injection.extensions.cdi.stream.CDIInjectionRegistryStream;
import org.hrodberaht.injection.extensions.junit.internal.JunitSQLContainerService;
import org.hrodberaht.injection.extensions.junit.internal.ProxyResourceCreator;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.internal.RegistrationInstanceSimple;
import org.hrodberaht.injection.spi.ContainerConfig;
import org.hrodberaht.injection.spi.ResourceCreator;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

/**
 * Support for CDI - light testing
 *
 * Beans can be wired with @Inject, @Resource, @PersistenceContext, @EJB
 *
 * Scope of the beans can be managed as Singleton using @Singleton, @ApplicationScoped, @Stateful all other will be managed as stateless
 *
 */
public abstract class JUnitStreamCDIContainerConfig implements ContainerConfig {


    private final CDIContainerConfigBase cdiContainerConfigBase;
    private final JunitSQLContainerService junitSQLContainerService;

    public JUnitStreamCDIContainerConfig(ResourceCreator resourceCreator) {
        cdiContainerConfigBase = new CDIContainerConfigBase(resourceCreator) {
            @Override
            public InjectContainer createContainer() {
                CDIInjectionRegistryStream registryStream = stream();
                registerStream(registryStream);
                for(RegistrationInstanceSimple instanceSimple:registryStream.getModule().getRegistrationsList()) {
                    if(instanceSimple.getScope() == null){
                        instanceSimple.scopeAs(registryStream.getCustomScanner().getScope(instanceSimple.getService()));
                    }
                }
                return createContainer(registryStream.getModule());
            }
        };
        junitSQLContainerService = new JunitSQLContainerService(cdiContainerConfigBase);
    }

    public JUnitStreamCDIContainerConfig() {
        this(new ProxyResourceCreator());
    }


    public final void addSQLSchemas(String schemaName, String packageBase) {
        junitSQLContainerService.addSQLSchemas(schemaName, packageBase);
    }

    public final void addSQLSchemas(String testPackageName, String schemaName, String packageBase) {
        junitSQLContainerService.addSQLSchemas(testPackageName, schemaName, packageBase);
    }

    /**
     * Register all resources to the stream and the JUnit test will automatically find them
     * @param cdiInjectionRegistryStream
     */
    protected abstract void registerStream(CDIInjectionRegistryStream cdiInjectionRegistryStream);

    /**
     * Creates a managed datasource that can be used in the container and tests. Can be reached as @Inject
     * @param dataSourceName
     * @return the managed data-source connected to a clean junit runner managed memory database
     */
    protected final DataSource createDataSource(String dataSourceName) {
        return cdiContainerConfigBase.createDataSource(dataSourceName);
    }

    /**
     * Creates a registration for a object that can be injected with @Resource
     * @param dataSourceName the data-source name
     * @param dataSource the data-source
     */
    protected final void addResource(String dataSourceName, Object dataSource) {
        cdiContainerConfigBase.addResource(dataSourceName, dataSource);
    }

    /**
     * Creates a JUnit runner managed EntityManager (cleans the managed automatically between test runs and flushes to ensure SQL is executed)
     * @param schemaName the name of the persistence unit
     * @param dataSourceName the data-source name
     * @param dataSource the data-source
     * @return
     */
    protected final EntityManager createEntityManager(String schemaName, String dataSourceName, DataSource dataSource){
        return cdiContainerConfigBase.createEntityManager(schemaName, dataSourceName, dataSource);
    }

    /**
     * Add the EntityManager so it can be Injected as CDi expects it to work via the @PersistenceContext
     * @param entityManagerName name of the PersistenceContext
     * @param entityManager the object bound to it
     */
    protected final void addPersistenceContext(String entityManagerName, EntityManager entityManager){
        cdiContainerConfigBase.addPersistenceContext(entityManagerName, entityManager);
    }

    @Override
    public final void cleanActiveContainer() {
        cdiContainerConfigBase.cleanActiveContainer();
    }

    @Override
    public final InjectionRegister getActiveRegister() {
        return cdiContainerConfigBase.getActiveRegister();
    }

    @Override
    public final InjectContainer getActiveContainer() {
        return cdiContainerConfigBase.getActiveContainer();
    }

    @Override
    public final void addSingletonActiveRegistry() {
        cdiContainerConfigBase.addSingletonActiveRegistry();
    }

    @Override
    public final ResourceCreator getResourceCreator() {
        return cdiContainerConfigBase.getResourceCreator();
    }

    @Override
    public final InjectContainer createContainer() {
        return cdiContainerConfigBase.createContainer();
    }
}

package org.hrodberaht.injection.extensions.junit.internal;

import org.hrodberaht.injection.extensions.junit.ejb.internal.InitialContextFactoryImpl;
import org.hrodberaht.injection.extensions.junit.internal.embedded.PersistenceResource;
import org.hrodberaht.injection.extensions.junit.internal.embedded.ResourceWatcher;
import org.hrodberaht.injection.spi.ResourceCreator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 20:38:41
 * @version 1.0
 * @since 1.0
 */
public class ProxyResourceCreator implements ResourceCreator<EntityManager, DataSourceProxy> {

    private static LocalResources LOCAL = new LocalResources();

    private Map<String, DataSourceProxy> DATASOURCES = new HashMap<String, DataSourceProxy>();
    private Map<String, EntityManager> ENTITYMANAGERS = new HashMap<String, EntityManager>();
    private DataSourceProvider provider = DataSourceProvider.HSQLDB;
    private DataSourcePersistence persistence = DataSourcePersistence.MEM;
    private ResourceWatcher resourceWatcher = null;
    private PersistenceResource resource = null;

    public enum DataSourceProvider {
        HSQLDB, H2
    }

    public enum DataSourcePersistence {
        MEM, RESTORABLE
    }

    public ProxyResourceCreator() {
    }

    public ProxyResourceCreator(DataSourceProvider provider, DataSourcePersistence persistence) {
        this.provider = provider;
        this.persistence = persistence;
    }

    public ProxyResourceCreator(DataSourceProvider provider, DataSourcePersistence persistence, ResourceWatcher resourceWatcher, PersistenceResource resource) {
        this.provider = provider;
        this.persistence = persistence;
        this.resourceWatcher = resourceWatcher;
        this.resource = resource;
    }

    public DataSourceProxy createDataSource(String dbName) {
        LocalResources localResources = LOCAL;
        if (!localResources.hasDataSource(dbName)) {
            DataSourceProxy dataSourceProxy = createDataSourceProxy(dbName);
            DATASOURCES.put(dbName, dataSourceProxy);
            localResources.putDataSource(dbName, dataSourceProxy);
            registerDataSourceInContext(dbName, dataSourceProxy);
            TDDLogger.log("Created dataSourceProxy " + dataSourceProxy);
            return dataSourceProxy;
        }
        DataSourceProxy dataSourceProxy = localResources.getDataSource(dbName);
        TDDLogger.log("Reused dataSourceProxy " + dataSourceProxy);
        DATASOURCES.put(dbName, dataSourceProxy);
        return dataSourceProxy;
    }

    @Override
    public DataSourceProxy createDataSource(String dbName, String dataSourceName) {
        return createDataSource(dataSourceName);
    }

    protected DataSourceProxy createDataSourceProxy(String dbName) {
        return new DataSourceProxy(dbName, provider, persistence, resource, resourceWatcher);
    }

    public DataSourceProxy getDataSource(String dbName) {
        return DATASOURCES.get(dbName);
    }

    public boolean hasDataSource(String dataSourceName) {
        return DATASOURCES.get(dataSourceName) != null;
    }

    public EntityManager createEntityManager(String name, String dataSourceName, DataSource dataSource) {
        LocalResources localResources = LOCAL;
        if (!localResources.hasEntityManager(name)) {
            registerDataSourceInContext(dataSourceName, dataSource);
            EntityManager entityManager = Persistence.createEntityManagerFactory(name).createEntityManager();
            TDDLogger.log("Created entity manager " + entityManager);
            ENTITYMANAGERS.put(name, entityManager);
            localResources.putEntityManager(name, entityManager);
            return entityManager;
        }
        EntityManager entityManager = localResources.getEntityManager(name);
        TDDLogger.log("Reused entity manager " + entityManager);
        ENTITYMANAGERS.put(name, entityManager);
        return entityManager;
    }

    private void registerDataSourceInContext(String dataSourceName, DataSource dataSource) {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                InitialContextFactoryImpl.class.getName());
        try {
            Context context = new InitialContext();
            context.bind(dataSourceName, dataSource);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<DataSourceProxy> getDataSources() {
        return DATASOURCES.values();
    }

    public Collection<EntityManager> getEntityManagers() {
        return ENTITYMANAGERS.values();
    }
}

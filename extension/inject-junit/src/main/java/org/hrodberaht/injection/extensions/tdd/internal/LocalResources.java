package org.hrodberaht.injection.extensions.tdd.internal;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-02-05 20:19
 * @created 1.0
 * @since 1.0
 */
public class LocalResources {

    private Map<String, DataSourceProxy> dataSources = new HashMap<String, DataSourceProxy>();
    private Map<String, EntityManager> entityManagers = new HashMap<String, EntityManager>();


    public boolean hasDataSource(String dbName) {
        return dataSources.containsKey(dbName);
    }

    public DataSourceProxy getDataSource(String dbName) {
        return dataSources.get(dbName);
    }

    public void putDataSource(String dbName, DataSourceProxy dataSourceProxy) {
        dataSources.put(dbName, dataSourceProxy);
    }

    public boolean hasEntityManager(String name) {
        return entityManagers.containsKey(name);
    }

    public EntityManager getEntityManager(String name) {
        return entityManagers.get(name);
    }


    public void putEntityManager(String name, EntityManager entityManager) {
        entityManagers.put(name, entityManager);
    }
}

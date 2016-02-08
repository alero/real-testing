package org.hrodberaht.inject.extension.cdi.inner;

import org.hrodberaht.inject.spi.ResourceCreator;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-11-28
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public class JSEResourceCreator implements ResourceCreator<EntityManager, DataSource> {

    Map<String, JSEDataSourceHandler> dataSourceHandlerMap = new ConcurrentHashMap<String, JSEDataSourceHandler>();

    public DataSource createDataSource(String dataSourceName) {
        JSEDataSourceHandler dataSourceHandler = new JSEDataSourceHandler(dataSourceName);
        dataSourceHandlerMap.put(dataSourceName, dataSourceHandler);
        return dataSourceHandler;
    }

    public boolean hasDataSource(String dataSourceName) {
        return dataSourceHandlerMap.containsKey(dataSourceName);
    }

    public DataSource getDataSource(String dataSourceName) {
        return dataSourceHandlerMap.get(dataSourceName);
    }

    public EntityManager createEntityManager(String schemaName, String dataSourceName, DataSource dataSource) {
        throw new IllegalAccessError("not supported");
    }

    public Collection<EntityManager> getEntityManagers() {
        return new ArrayList<EntityManager>();
    }

    public Collection<DataSource> getDataSources() {
        return new ArrayList<DataSource>();
    }
}

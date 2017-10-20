package org.hrodberaht.injection.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class InjectionResources {
    private Map<String, ResourceDataSource> dataSources = new HashMap<>();
    private List<ResourceEntityManager> entityManagers = new ArrayList<>();

    public List<ResourceDataSource> getDataSources() {
        return new ArrayList<>(dataSources.values());
    }

    public ResourceDataSource createDataSource(String aDataSource) {
        ResourceDataSource dataSource = new ResourceDataSource(aDataSource);
        dataSources.put(aDataSource, dataSource);
        return dataSource;
    }

    public ResourceEntityManager createEntityManager(String aJPAResource, String aDataSource) {
        ResourceDataSource resourceDataSource = dataSources.get(aDataSource);
        if (resourceDataSource == null) {
            throw new IllegalAccessError("aDataSource not found:" + aDataSource);
        }
        ResourceEntityManager dataSource = new ResourceEntityManager(aJPAResource, resourceDataSource);
        entityManagers.add(dataSource);
        return dataSource;
    }

    public List<ResourceEntityManager> getEntityManagers() {
        return entityManagers;
    }
}

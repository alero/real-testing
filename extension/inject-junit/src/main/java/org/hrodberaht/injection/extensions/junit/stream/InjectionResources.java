package org.hrodberaht.injection.extensions.junit.stream;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class InjectionResources {
    private List<ResourceDataSource> dataSources = new ArrayList<>();

    public List<ResourceDataSource> getDataSources() {
        return dataSources;
    }

    public ResourceDataSource createDataSource(String aDataSource) {
        ResourceDataSource dataSource = new ResourceDataSource(aDataSource);
        dataSources.add(dataSource);
        return dataSource;
    }
}

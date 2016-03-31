package org.hrodberaht.injection.stream;

/**
 * Created by alexbrob on 2016-03-31.
 */
public class ResourceEntityManager {
    private String name;
    private ResourceDataSource resourceDataSource;

    public ResourceEntityManager(String name, ResourceDataSource resourceDataSource) {
        this.name = name;
        this.resourceDataSource = resourceDataSource;
    }

    public String getName() {
        return name;
    }

    public ResourceDataSource getResourceDataSource() {
        return resourceDataSource;
    }
}

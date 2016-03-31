package org.hrodberaht.injection.stream;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class ResourceDataSource {

    private String name;
    private String packageName;
    private String path;

    public String getName() {
        return name;
    }

    public ResourceDataSource(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPath() {
        return path;
    }

    public void loadSQLSchemas(String packageName, String path) {
        this.packageName = packageName;
        this.path = path;
    }
}

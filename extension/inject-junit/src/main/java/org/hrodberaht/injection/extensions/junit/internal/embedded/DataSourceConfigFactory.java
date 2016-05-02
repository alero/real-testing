package org.hrodberaht.injection.extensions.junit.internal.embedded;

import org.hrodberaht.injection.extensions.junit.internal.ProxyResourceCreator;
import org.hrodberaht.injection.extensions.junit.internal.embedded.vendors.HsqlBDDataSourceConfigurationMem;
import org.hrodberaht.injection.extensions.junit.internal.embedded.vendors.HsqlBDDataSourceConfigurationRestorable;
import org.hrodberaht.injection.spi.DataSourceProxyInterface;

public class DataSourceConfigFactory {

    private DataSourceProxyInterface dataSourceProxyInterface;
    private ResourceWatcher resourceWatcher;
    private PersistenceResource resource;
    private String dbName;

    public DataSourceConfigFactory(DataSourceProxyInterface dataSourceProxyInterface,
                                   ResourceWatcher resourceWatcher, PersistenceResource resource, String dbName) {
        this.dataSourceProxyInterface = dataSourceProxyInterface;
        this.resourceWatcher = resourceWatcher;
        this.resource = resource;
        this.dbName = dbName;
    }

    public DataSourceConfiguration createConfiguration(
            ProxyResourceCreator.DataSourceProvider provider,
            ProxyResourceCreator.DataSourcePersistence persistence) {
        if (provider == ProxyResourceCreator.DataSourceProvider.HSQLDB) {
            if (persistence == ProxyResourceCreator.DataSourcePersistence.MEM) {
                return new HsqlBDDataSourceConfigurationMem(dbName);
            } else if (persistence == ProxyResourceCreator.DataSourcePersistence.RESTORABLE) {
                return new HsqlBDDataSourceConfigurationRestorable(dbName, resourceWatcher, resource, dataSourceProxyInterface);
            }
        }
        throw new RuntimeException("not supported databasetype");
    }


}

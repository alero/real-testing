package org.hrodberaht.injection.extensions.junit.internal.embedded;

import org.hrodberaht.injection.extensions.junit.internal.ProxyResourceCreator;
import org.hrodberaht.injection.extensions.junit.internal.embedded.vendors.HsqlBDDataSourceConfigurationRestorable;
import org.hrodberaht.injection.spi.DataSourceProxyInterface;

public class DataSourceConfigFactory {

    private DataSourceProxyInterface dataSourceProxyInterface;
    private ResourceWatcher resourceWatcher;
    private String dbName;

    public DataSourceConfigFactory(DataSourceProxyInterface dataSourceProxyInterface,
                                   ResourceWatcher resourceWatcher,  String dbName) {
        this.dataSourceProxyInterface = dataSourceProxyInterface;
        this.resourceWatcher = resourceWatcher;
        this.dbName = dbName;
    }

    public DataSourceConfiguration createConfiguration(
            ProxyResourceCreator.DataSourceProvider provider,
            ProxyResourceCreator.DataSourcePersistence persistence) {
        if (provider == ProxyResourceCreator.DataSourceProvider.HSQLDB) {
            if (persistence == ProxyResourceCreator.DataSourcePersistence.MEM) {
                return new HsqlBDDataSourceConfigurationRestorable(dbName, null);
            } else if (persistence == ProxyResourceCreator.DataSourcePersistence.RESTORABLE) {
                return new HsqlBDDataSourceConfigurationRestorable(dbName, resourceWatcher);
            }
        }
        throw new RuntimeException("not supported databasetype");
    }


}

package org.hrodberaht.injection.plugin.junit.datasource;

import org.hrodberaht.injection.plugin.junit.ResourceWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ProxyResourceCreator implements DatasourceCreator{

    private static final Logger LOG = LoggerFactory.getLogger(ProxyResourceCreator.class);

    public enum DataSourceProvider {
        HSQLDB, H2
    }

    public enum DataSourcePersistence {
        MEM, RESTORABLE
    }

    final Map<String, DataSourceProxy> DATASOURCES = new HashMap<>();

    private final DataSourceProvider provider;
    private final DataSourcePersistence persistence;
    private final ResourceWatcher resourceWatcher;

    public ProxyResourceCreator(DataSourceProvider provider, DataSourcePersistence persistence) {
        this(provider, persistence, null);
    }

    public ProxyResourceCreator(DataSourceProvider provider, DataSourcePersistence persistence, ResourceWatcher resourceWatcher) {
        this.provider = provider;
        this.persistence = persistence;
        this.resourceWatcher = resourceWatcher;
    }



    public DataSourceProxy createDataSource(String dataSourceName) {

        if (!hasDataSource(dataSourceName)) {
            DataSourceProxy dataSourceProxy = createDataSourceProxy(dataSourceName);
            DATASOURCES.put(dataSourceName, dataSourceProxy);
            LOG.info("Created dataSourceProxy " + dataSourceProxy);
            return dataSourceProxy;
        }
        DataSourceProxy dataSourceProxy = DATASOURCES.get(dataSourceName);
        LOG.info("Reused dataSourceProxy " + dataSourceProxy);
        DATASOURCES.put(dataSourceName, dataSourceProxy);
        return dataSourceProxy;
    }


    private DataSourceProxy createDataSourceProxy(String dataSourceName) {
        return new DataSourceProxy(dataSourceName, provider, persistence, resourceWatcher);
    }

    private boolean hasDataSource(String dataSourceName) {
        return DATASOURCES.get(dataSourceName) != null;
    }





}

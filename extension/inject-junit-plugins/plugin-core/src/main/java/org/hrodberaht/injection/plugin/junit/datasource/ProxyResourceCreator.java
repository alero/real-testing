package org.hrodberaht.injection.plugin.junit.datasource;

import org.hrodberaht.injection.plugin.context.InitialContextFactoryImpl;
import org.hrodberaht.injection.plugin.datasource.embedded.ResourceWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
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

    final Map<String, DataSourceProxy> DATASOURCES = new HashMap<>();

    private final DataSourceProvider provider;
    private final DataSourcePersistence persistence;
    private final ResourceWatcher resourceWatcher;

    public ProxyResourceCreator(DataSourceProvider provider, DataSourcePersistence persistence) {
        this(provider, persistence, null);
        initContext();
    }

    public enum DataSourceProvider {
        HSQLDB, H2
    }

    public enum DataSourcePersistence {
        MEM, RESTORABLE
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
            registerDataSourceInContext(dataSourceName, dataSourceProxy);
            LOG.info("Created dataSourceProxy " + dataSourceProxy);
            return dataSourceProxy;
        }
        DataSourceProxy dataSourceProxy = DATASOURCES.get(dataSourceName);
        LOG.info("Reused dataSourceProxy " + dataSourceProxy);
        DATASOURCES.put(dataSourceName, dataSourceProxy);
        return dataSourceProxy;
    }


    public DataSourceProxy createDataSource(String dbName, String dataSourceName) {
        return createDataSource(dataSourceName);
    }

    protected DataSourceProxy createDataSourceProxy(String dataSourceName) {
        return new DataSourceProxy(dataSourceName, provider, persistence, resourceWatcher);
    }

    public DataSourceProxy getDataSource(String dbName) {
        return DATASOURCES.get(dbName);
    }

    public boolean hasDataSource(String dataSourceName) {
        return DATASOURCES.get(dataSourceName) != null;
    }


    private void registerDataSourceInContext(String dataSourceName, DataSource dataSource) {

        try {
            Context context = new InitialContext();
            context.bind(dataSourceName, dataSource);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
    private void initContext() {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, InitialContextFactoryImpl.class.getName());
    }

}

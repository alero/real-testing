/*
 * Copyright (c) 2017 org.hrodberaht
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hrodberaht.injection.plugin.junit.datasource;

import org.hrodberaht.injection.plugin.junit.ResourceWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 20:38:41
 * @version 1.0
 * @since 1.0
 */
public class ProxyResourceCreator implements DatasourceCreator {

    private static final Logger LOG = LoggerFactory.getLogger(ProxyResourceCreator.class);

    public enum DataSourceProvider {
        HSQLDB, H2
    }

    public enum DataSourcePersistence {
        MEM, RESTORABLE
    }

    final Map<String, DataSourceProxyInterface> dataSources = new HashMap<>();

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

    public DataSourceProxyInterface createDataSource(String dataSourceName) {
        if (!hasDataSource(dataSourceName)) {
            DataSourceProxyInterface dataSourceProxy = createDataSourceProxy(dataSourceName);
            dataSources.put(dataSourceName, dataSourceProxy);
            LOG.info("Created dataSourceProxy {}", dataSourceProxy);
            return dataSourceProxy;
        }
        DataSourceProxyInterface dataSourceProxy = dataSources.get(dataSourceName);
        LOG.info("Reused dataSourceProxy {}", dataSourceProxy);
        dataSources.put(dataSourceName, dataSourceProxy);

        return dataSourceProxy;
    }

    @Override
    public Collection<DataSourceProxyInterface> getDataSources() {
        return dataSources.values();
    }


    private DataSourceProxyInterface createDataSourceProxy(String dataSourceName) {
        return new SimpleDataSourceProxy(dataSourceName, provider, persistence, resourceWatcher);
    }

    private boolean hasDataSource(String dataSourceName) {
        return dataSources.get(dataSourceName) != null;
    }


}

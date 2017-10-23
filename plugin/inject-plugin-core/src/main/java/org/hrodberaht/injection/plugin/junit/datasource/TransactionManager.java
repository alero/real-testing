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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 * 2011-02-05 19:26
 * @created 1.0
 * @since 1.0
 */
public class TransactionManager {

    private final Logger LOG = LoggerFactory.getLogger(TransactionManager.class);
    private final ThreadLocal<DataSources> DATA_SOURCES = new ThreadLocal<>();
    private final ProxyResourceCreator proxyResourceCreator;


    public TransactionManager(ProxyResourceCreator proxyResourceCreator) {
        this.proxyResourceCreator = proxyResourceCreator;
    }


    public void endTransaction() {
        DataSources dataSources = DATA_SOURCES.get();
        dataSources.dataSourceList.forEach(dataSource -> {
            dataSource.clearDataSource();
            LOG.debug("dataSource rollback " + dataSource);
        });
    }

    public void endTransactionCommit() {
        DataSources dataSources = DATA_SOURCES.get();
        dataSources.dataSourceList.forEach(dataSource -> {
            dataSource.commitDataSource();
            LOG.debug("dataSource committed " + dataSource);
        });
    }

    public void beginTransaction() {

        DATA_SOURCES.set(new DataSources(proxyResourceCreator.DATASOURCES.values()));
        DataSources dataSources = DATA_SOURCES.get();
        dataSources.dataSourceList.forEach(dataSource -> {
            try {
                dataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            LOG.debug("dataSource Begin " + dataSource);

        });
    }

    private class DataSources {
        private final List<DataSourceProxy> dataSourceList;

        public DataSources(Collection<DataSourceProxy> dataSourceList) {
            this.dataSourceList = new ArrayList<>(dataSourceList);
        }
    }
}

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
    private final ThreadLocal<DataSources> dataSourcesThreadLocal = new ThreadLocal<>();
    private final DatasourceCreator resourceCreator;


    public TransactionManager(DatasourceCreator resourceCreator) {
        this.resourceCreator = resourceCreator;
    }


    public void endTransaction() {
        LOG.info("endTransaction on thread {}", Thread.currentThread().getName());
        DataSources dataSources = dataSourcesThreadLocal.get();
        dataSources.dataSourceList.forEach(dataSource -> {
            try {
                LOG.info("dataSource rollback {} - {}", dataSource, dataSource.getConnection());
            } catch (SQLException e) {
                throw new DataSourceRuntimeException(e);
            }
            dataSource.clearDataSource();
        });
    }

    public void endTransactionCommit() {
        LOG.info("endTransactionCommit on thread {}", Thread.currentThread().getName());
        DataSources dataSources = dataSourcesThreadLocal.get();
        dataSources.dataSourceList.forEach(dataSource -> {
            try {
                LOG.info("dataSource commit {} - {}", dataSource, dataSource.getConnection());
            } catch (SQLException e) {
                throw new DataSourceRuntimeException(e);
            }
            dataSource.commitDataSource();
        });
    }

    public void beginTransaction() {
        LOG.info("beginTransaction on thread {}", Thread.currentThread().getName());
        dataSourcesThreadLocal.set(new DataSources(resourceCreator.getDataSources()));
        DataSources dataSources = dataSourcesThreadLocal.get();
        dataSources.dataSourceList.forEach(dataSource -> {
            try {
                LOG.info("dataSource begin {} - {} on thread {}", dataSource, dataSource.getConnection(), Thread.currentThread().getName());
            } catch (SQLException e) {
                throw new DataSourceRuntimeException(e);
            }

        });
    }

    private class DataSources {
        private final List<DataSourceProxyInterface> dataSourceList;

        public DataSources(Collection<DataSourceProxyInterface> dataSourceList) {
            this.dataSourceList = new ArrayList<>(dataSourceList);
        }
    }
}

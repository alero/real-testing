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

import javax.sql.DataSource;

/**
 * Created by alexbrob on 2016-03-01.
 */
public class DatasourceContainerService {

    private static final Logger LOG = LoggerFactory.getLogger(DatasourceContainerService.class);

    private DataSource dataSource;

    public DatasourceContainerService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addSQLSchemas(String schemaName, String packageBase) {
        DataSourceExecution sourceExecution = new DataSourceExecution(dataSource);
        LOG.debug("checking schema {}:{}", schemaName, packageBase);
        if (!sourceExecution.isInitiated(schemaName, packageBase)) {
            LOG.info("add any found SQLSchemas for {}:{}", schemaName, packageBase);
            sourceExecution.addSQLSchemas(schemaName, packageBase);
        }
    }
}

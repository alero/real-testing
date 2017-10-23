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

package org.hrodberaht.injection.plugin.datasource;

import org.hrodberaht.injection.plugin.junit.datasource.DatasourceCreator;
import org.hrodberaht.injection.core.spi.JavaResourceCreator;

import javax.sql.DataSource;

public class DatasourceResourceCreator implements JavaResourceCreator<DataSource> {

    private final DatasourceCreator datasourceCreator;

    public DatasourceResourceCreator(DatasourceCreator datasourceCreator) {
        this.datasourceCreator = datasourceCreator;
    }

    public Class getType() {
        return DataSource.class;
    }

    @Override
    public DataSource create(String dataSourceName) {
        return datasourceCreator.createDataSource(dataSourceName);
    }

    @Override
    public DataSource create() {
        return datasourceCreator.createDataSource("defaultDataSource");
    }

    @Override
    public DataSource create(String name, DataSource instance) {
        return instance;
    }

    @Override
    public DataSource create(DataSource instance) {
        return instance;
    }
}

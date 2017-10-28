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

package org.hrodberaht.injection.plugin.datasource.embedded;

import org.hrodberaht.injection.plugin.junit.datasource.DataSourceProxyInterface;
import org.hrodberaht.injection.plugin.datasource.embedded.vendors.HsqlBDDataSourceConfigurationRestorable;
import org.hrodberaht.injection.plugin.junit.ResourceWatcher;
import org.hrodberaht.injection.plugin.junit.datasource.ProxyResourceCreator;


public class DataSourceConfigFactory {

    private DataSourceProxyInterface dataSourceProxyInterface;
    private ResourceWatcher resourceWatcher;
    private String dbName;

    public DataSourceConfigFactory(DataSourceProxyInterface dataSourceProxyInterface,
                                   ResourceWatcher resourceWatcher, String dbName) {
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

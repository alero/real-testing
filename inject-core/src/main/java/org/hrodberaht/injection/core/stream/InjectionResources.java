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

package org.hrodberaht.injection.core.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alexbrob on 2016-03-30.
 */
public class InjectionResources {
    private Map<String, ResourceDataSource> dataSources = new HashMap<>();
    private List<ResourceEntityManager> entityManagers = new ArrayList<>();

    public List<ResourceDataSource> getDataSources() {
        return new ArrayList<>(dataSources.values());
    }

    public ResourceDataSource createDataSource(String aDataSource) {
        ResourceDataSource dataSource = new ResourceDataSource(aDataSource);
        dataSources.put(aDataSource, dataSource);
        return dataSource;
    }

    public ResourceEntityManager createEntityManager(String aJPAResource, String aDataSource) {
        ResourceDataSource resourceDataSource = dataSources.get(aDataSource);
        if (resourceDataSource == null) {
            throw new IllegalAccessError("aDataSource not found:" + aDataSource);
        }
        ResourceEntityManager dataSource = new ResourceEntityManager(aJPAResource, resourceDataSource);
        entityManagers.add(dataSource);
        return dataSource;
    }

    public List<ResourceEntityManager> getEntityManagers() {
        return entityManagers;
    }
}

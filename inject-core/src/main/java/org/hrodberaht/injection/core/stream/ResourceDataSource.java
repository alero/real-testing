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

/**
 * Created by alexbrob on 2016-03-30.
 */
public class ResourceDataSource {

    private String name;
    private String packageName;
    private String path;

    public String getName() {
        return name;
    }

    public ResourceDataSource(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPath() {
        return path;
    }

    public void loadSQLSchemas(String packageName, String path) {
        this.packageName = packageName;
        this.path = path;
    }
}

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

package org.hrodberaht.injection.core.spi;

public class ResourceKey {
    private final String name;
    private final Class type;


    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }

    public static ResourceKey of(String name, Class type) {
        return new ResourceKey(name, type);
    }

    private ResourceKey(String name, Class type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceKey key = (ResourceKey) o;

        if (name != null ? !name.equals(key.name) : key.name != null) return false;
        return type != null ? type.equals(key.type) : key.type == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResourceKey{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}

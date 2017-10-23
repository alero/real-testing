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

package org.hrodberaht.injection.internal.annotation;

import org.hrodberaht.injection.internal.InjectionKey;
import org.hrodberaht.injection.internal.ServiceRegister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by alexbrob on 2016-02-17.
 */
public class ServiceRegistryForInjection {

    private Map<InjectionKey, InjectionMetaDataBase> injectionMetaDataCache = null;

    public ServiceRegistryForInjection(Map<InjectionKey, InjectionMetaDataBase> injectionMetaDataCache) {
        this.injectionMetaDataCache = injectionMetaDataCache;
    }

    public Collection<ServiceRegister> getServiceRegisterCollection() {
        Collection<ServiceRegister> registers = new ArrayList<>();
        for (InjectionMetaDataBase injectionMetaDataBase : injectionMetaDataCache.values()) {
            if (injectionMetaDataBase.getServiceRegister() != null) {
                registers.add(injectionMetaDataBase.getServiceRegister());
            }
        }
        return registers;
    }

    public Collection<InjectionMetaDataBase> getInjectionMetaDataBaseCollection() {
        Collection<InjectionMetaDataBase> registers = new ArrayList<>(injectionMetaDataCache.values());
        return registers;
    }

    public ServiceRegister get(InjectionKey key) {
        InjectionMetaDataBase injectionMetaDataBase = injectionMetaDataCache.get(key);
        if (injectionMetaDataBase != null) {
            return injectionMetaDataBase.getServiceRegister();
        }
        return null;
    }

    public boolean containsKey(InjectionKey key) {
        InjectionMetaDataBase injectionMetaDataBase = injectionMetaDataCache.get(key);
        if (injectionMetaDataBase != null) {
            return injectionMetaDataBase.getServiceRegister() != null;
        }
        return false;
    }

    public Set<InjectionKey> keySet() {
        return injectionMetaDataCache.keySet();
    }

    public void put(InjectionKey injectionKey, ServiceRegister clone) {
        injectionMetaDataCache.get(injectionKey).setServiceRegister(clone);
    }
}

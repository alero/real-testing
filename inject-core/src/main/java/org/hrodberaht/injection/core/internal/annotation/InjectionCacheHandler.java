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

package org.hrodberaht.injection.core.internal.annotation;

import org.hrodberaht.injection.core.internal.InjectionKey;
import org.hrodberaht.injection.core.internal.exception.InjectRuntimeException;

import java.util.Map;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-maj-29 15:25:40
 * @version 1.0
 * @since 1.0
 */
public class InjectionCacheHandler {


    private Map<InjectionKey, InjectionMetaDataBase> injectionMetaDataCache = null;

    public InjectionCacheHandler(Map<InjectionKey, InjectionMetaDataBase> injectionMetaDataCache) {
        this.injectionMetaDataCache = injectionMetaDataCache;
    }

    public void put(InjectionMetaData injectionMetaData) {
        if (injectionMetaData.getKey() == null) {
            throw new InjectRuntimeException("injectionMetaData.getKey() is null");
        }
        injectionMetaDataCache.put(injectionMetaData.getKey(), new InjectionMetaDataBase(injectionMetaData));
    }

    public void clear(InjectionMetaData injectionMetaData) {
        if (injectionMetaData.getKey() == null) {
            throw new InjectRuntimeException("injectionMetaData.getKey() is null");
        }
        injectionMetaDataCache.remove(injectionMetaData.getKey());
    }

    public InjectionMetaData find(InjectionMetaData injectionMetaData) {
        if (injectionMetaData.getKey() == null) {
            throw new InjectRuntimeException("injectionMetaData.getKey() is null");
        }
        InjectionMetaData foundMetaData = nullSageGetInjectionMetaData(injectionMetaData);
        if (foundMetaData != null) {
            return foundMetaData;
        }
        // Try to find similar Injection's that can handle the injection.
        // Will search with isAssignableFrom as final way
        for (InjectionMetaDataBase anInjectionMetaDataCache : injectionMetaDataCache.values()) {
            InjectionMetaData metaData = anInjectionMetaDataCache.getInjectionMetaData();
            if (injectionMetaData.canInject(metaData)) {
                return metaData;
            }
        }

        return null;

    }

    private InjectionMetaData nullSageGetInjectionMetaData(InjectionMetaData injectionMetaData) {
        InjectionMetaDataBase injectionMetaDataBase = injectionMetaDataCache.get(injectionMetaData.getKey());
        if (injectionMetaDataBase != null) {
            return injectionMetaDataBase.getInjectionMetaData();
        }
        return null;
    }

    public int size() {
        return injectionMetaDataCache.size();
    }
}

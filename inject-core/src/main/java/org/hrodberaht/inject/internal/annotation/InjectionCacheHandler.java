/*
 * ~ Copyright (c) 2010.
 *   ~ Licensed under the Apache License, Version 2.0 (the "License");
 *   ~ you may not use this file except in compliance with the License.
 *   ~ You may obtain a copy of the License at
 *   ~
 *   ~        http://www.apache.org/licenses/LICENSE-2.0
 *   ~
 *   ~ Unless required by applicable law or agreed to in writing, software
 *   ~ distributed under the License is distributed on an "AS IS" BASIS,
 *   ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   ~ See the License for the specific language governing permissions and limitations under the License.
 */

package org.hrodberaht.inject.internal.annotation;

import org.hrodberaht.inject.internal.InjectionKey;
import org.hrodberaht.inject.internal.exception.InjectRuntimeException;

import java.util.Map;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-29 15:25:40
 * @version 1.0
 * @since 1.0
 */
public class InjectionCacheHandler {


    private Map<InjectionKey, InjectionMetaData> injectionMetaDataCache = null;

    public InjectionCacheHandler(Map<InjectionKey, InjectionMetaData> injectionMetaDataCache) {
        this.injectionMetaDataCache = injectionMetaDataCache;
    }

    public void put(InjectionMetaData injectionMetaData) {
        if (injectionMetaData.getKey() == null) {
            throw new InjectRuntimeException("injectionMetaData.getKey() is null");
        }
        injectionMetaDataCache.put(injectionMetaData.getKey(), injectionMetaData);
    }

    public InjectionMetaData find(InjectionMetaData injectionMetaData) {
        if (injectionMetaData.getKey() == null) {
            throw new InjectRuntimeException("injectionMetaData.getKey() is null");
        }
        InjectionMetaData foundMetaData = injectionMetaDataCache.get(injectionMetaData.getKey());
        if (foundMetaData != null) {
            return foundMetaData;
        }
        // Try to find similar Injection's that can handle the injection.
        // Will search with isAssignableFrom as final way
        for (InjectionMetaData anInjectionMetaDataCache : injectionMetaDataCache.values()) {
            InjectionMetaData metaData = anInjectionMetaDataCache;
            if (injectionMetaData.canInject(metaData)) {
                return metaData;
            }
        }

        return null;

    }

}
